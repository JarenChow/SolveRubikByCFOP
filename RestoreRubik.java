import java.util.Arrays;

/**
 * Created by Administrator on 2015/12/9 0009.
 */
public class RestoreRubik {

    public static void main(String[] args) {
        Rubik rubik = Rubik.getInstance();
        rubik.init();
        String disruptedData = rubik.getDisruptedData(15); // ��ô��Ҳ���
        System.out.println("���Ҳ���: " + disruptedData);
        rubik.handleTurn(disruptedData); // ����ħ��
//        ��������ַ�ʽ����¼��ħ����Ϣ
//        rubik.getUpFace().init(Rubik.Yellow, Rubik.Yellow, Rubik.Yellow, Rubik.Yellow, Rubik.Yellow,
//                Rubik.Yellow, Rubik.Yellow, Rubik.Yellow, Rubik.Yellow);
//        rubik.getFrontFace().init(Rubik.Blue, Rubik.Blue, Rubik.Blue, Rubik.Blue, Rubik.Blue,
//                Rubik.Blue, Rubik.Blue, Rubik.Blue, Rubik.Blue);
//        rubik.getRightFace().init(Rubik.Red, Rubik.Red, Rubik.Red, Rubik.Red, Rubik.Red,
//                Rubik.Red, Rubik.Red, Rubik.Red, Rubik.Red);
//        rubik.getBackFace().init(Rubik.Green, Rubik.Green, Rubik.Green, Rubik.Green, Rubik.Green,
//                Rubik.Green, Rubik.Green, Rubik.Green, Rubik.Green);
//        rubik.getLeftFace().init(Rubik.Orange, Rubik.Orange, Rubik.Orange, Rubik.Orange, Rubik.Orange,
//                Rubik.Orange, Rubik.Orange, Rubik.Orange, Rubik.Orange);
//        rubik.getDownFace().init(Rubik.White, Rubik.White, Rubik.White, Rubik.White, Rubik.White,
//                Rubik.White, Rubik.White, Rubik.White, Rubik.White);
        rubik.cross();
        System.out.println("Cross����: " + rubik.getCrossData());
        rubik.f2l();
        System.out.println("F2L���ĸ�����(��˳��ִ��): " + Arrays.toString(rubik.getF2lData()));
        rubik.oll();
        System.out.println("OLL����: " + rubik.getOllData());
        rubik.pll();
        System.out.println("PLL����: " + rubik.getPllData());
        System.out.println("ħ���Ƿ�ԭ: " + rubik.isRubikFinished());
    }
}
