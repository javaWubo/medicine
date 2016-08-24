package com.wuch.medicine.dao.common;


import com.mysql.jdbc.log.LogFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.xml.bind.PropertyException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: zhanglu7
 * Date: 14-11-25
 * Time: 上午9:31
 * To change this template use File | Settings | File Templates.
 */

/**
*
*
**/
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class PaginationInterceptor implements Interceptor {

    private String dialect = "";    //数据库方言
    private String pageSqlId = ""; //mapper.xml中需要拦截的ID(正则匹配)
    private static Logger logger = LoggerFactory.getLogger(PaginationInterceptor.class);

    @Override
    public Object intercept(Invocation ivk) throws Throwable {
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            if (ivk.getTarget() instanceof RoutingStatementHandler) {
                RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
                BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler, "delegate");
                MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate, "mappedStatement");

                if (mappedStatement.getId().matches(pageSqlId)) { //拦截需要分页的SQL
                    BoundSql boundSql = delegate.getBoundSql();
                    Object parameterObject = boundSql.getParameterObject();//分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
                    if (parameterObject == null) {
                        throw new NullPointerException("parameterObject尚未实例化！");
                    } else {
                        Connection connection = (Connection) ivk.getArgs()[0];
                        String sql = boundSql.getSql();
                        String countSql = "select count(0) from (" + sql + ") as tmp_count"; //记录统计
                        //logger.info("countSql=" + countSql);
                        countStmt = connection.prepareStatement(countSql);
                        BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), parameterObject);
                        setParameters(countStmt, mappedStatement, countBS, parameterObject);
                        rs = countStmt.executeQuery();
                        int count = 0;
                        if (rs.next()) {
                            count = rs.getInt(1);
                        }
                        rs.close();
                        countStmt.close();
                        Map paras = (Map) parameterObject;
                        Pagination page = (Pagination) paras.get("page");
                        if (page == null)
                            page = new Pagination();
                        page.setTotalCount(count);
                        page.setTotalPage(count % page.getPageSize() == 0 ? count / page.getPageSize() : count / page.getPageSize() + 1);
                        if (page.getTotalPage() < page.getCurrentPage() && page.getTotalPage() > 0) {
                            page.setCurrentPage(page.getTotalPage());
                        }
                        String pageSql = generatePageSql(sql, page);
                        ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql); //将分页sql语句反射回BoundSql.
                    }
                }
            }
            return ivk.proceed();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (countStmt != null) {
                    countStmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                throw e;
            }
        }
    }

    /**
     * 对SQL参数(?)设值,参考org.apache.ibatis.executor.parameter.DefaultParameterHandler
     *
     * @param ps
     * @param mappedStatement
     * @param boundSql
     * @param parameterObject
     * @throws SQLException
     */
    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) throws SQLException {
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            Configuration configuration = mappedStatement.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    PropertyTokenizer prop = new PropertyTokenizer(propertyName);
                    if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX) && boundSql.hasAdditionalParameter(prop.getName())) {
                        value = boundSql.getAdditionalParameter(prop.getName());
                        if (value != null) {
                            value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
                        }
                    } else {
                        value = metaObject == null ? null : metaObject.getValue(propertyName);
                    }
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    if (typeHandler == null) {
                        throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName + " of statement " + mappedStatement.getId());
                    }
                    typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
                }
            }
        }
    }

    /**
     * 根据数据库方言，生成特定的分页sql
     *
     * @param sql
     * @param page
     * @return
     */
    private String generatePageSql(String sql, Pagination page) {
        if (page != null && !StringUtils.isEmpty(dialect)) {
            StringBuffer pageSql = new StringBuffer();
            if ("mysql".equals(dialect)) {
                pageSql.append(sql);
                if (!StringUtils.isEmpty(page.getSort())) {
                    pageSql.append(" order by " + page.getSort() + " " + page.getOrder());
                }
                pageSql.append(" limit " + ((page.getCurrentPage() - 1) * page.getPageSize()) + "," + page.getPageSize());
            } else if ("oracle".equals(dialect)) {
                pageSql.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
                pageSql.append(sql);
                if (!StringUtils.isEmpty(page.getSort())) {
                    pageSql.append(" order by " + page.getSort() + " " + page.getOrder());
                }
                pageSql.append(") as tmp_tb where ROWNUM<=");
                pageSql.append(((page.getCurrentPage() - 1) * page.getPageSize()) + page.getPageSize());
                pageSql.append(") where row_id>");
                pageSql.append((page.getCurrentPage() - 1) * page.getPageSize());
            }
            return pageSql.toString();
        } else {
            return sql;
        }
    }

    /**
    *
    * @param arg0
    *
    **/
    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }

    /**
    *
    * setter of properties
    * @param p
    *
    **/
    public void setProperties(Properties p) {
        dialect = p.getProperty("dialect");
        if (StringUtils.isEmpty(dialect)) {
            try {
                throw new PropertyException("dialect property is not found!");
            } catch (PropertyException e) {
                logger.error("PropertyException", e);
            }
        }
        pageSqlId = p.getProperty("pageSqlId");
        if (StringUtils.isEmpty(pageSqlId)) {
            try {
                throw new PropertyException("pageSqlId property is not found!");
            } catch (PropertyException e) {
                logger.error("PropertyException", e);
            }
        }
    }
}
