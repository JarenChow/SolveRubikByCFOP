import java.util.Arrays;

/**
 * Created by Administrator on 2015/12/9 0009.
 */
public class RestoreRubik {

    public static void main(String[] args) {
        Rubik rubik = Rubik.getInstance();
        rubik.init();
        String disruptedData = rubik.getDisruptedData(15); // 获得打乱步骤
        System.out.println("打乱步骤: " + disruptedData);
        rubik.handleTurn(disruptedData); // 打乱魔方
//        如果用这种方式就是录入魔方信息
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
        System.out.println("Cross数据: " + rubik.getCrossData());
        rubik.f2l();
        System.out.println("F2L的四个数据(按顺序执行): " + Arrays.toString(rubik.getF2lData()));
        rubik.oll();
        System.out.println("OLL数据: " + rubik.getOllData());
        rubik.pll();
        System.out.println("PLL数据: " + rubik.getPllData());
        System.out.println("魔方是否还原: " + rubik.isRubikFinished());
    }
}
