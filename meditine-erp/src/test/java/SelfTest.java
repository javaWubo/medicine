import java.util.Arrays;

/**
 * Created by wubo5 on 2017/2/4.
 */
public class SelfTest {
    private transient static Object[] elementData={"1"};
    public static void main(String[] args) {
        elementData = Arrays.copyOf(elementData,100);
        for(int i = 0;i<elementData.length;i++){
            System.out.println(elementData[i]);
        }
    }
}
