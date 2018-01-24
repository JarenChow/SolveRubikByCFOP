import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jia en on 2015/11/14 0014.
 * 魔方还原的算法
 * Rubik    getInstance()       获得Rubik的唯一实例
 * void     init()              初始化
 * Face     getFace().init()    初始化面的值,up,front,right,back,left,down
 * String   getDisruptedData()  获得打乱步骤的数据
 * void     handleTurn(String)  打乱魔方
 * void     cross()             运行cross
 * void     f2l()               运行f2l
 * void     oll()               运行oll
 * void     pll()               运行pll
 * String   getCrossData()      获得Cross的数据
 * String   getF2lData()        获得F2L的数据
 * String   getPllData()        获得PLL的数据
 * String   getOllData()        获得OLL的数据
 * boolean  isRubikFinished()   判断魔方是否复原
 */
public class Rubik {

    public static final int Yellow = 1; // Yellow

    public static final int Blue = 2; // Blue

    public static final int Red = 3; // Red

    public static final int Green = 4; // Green

    public static final int Orange = 5; // Orange

    public static final int White = 6; // White

    public static final int None = 0; // None，无颜色

    // 上、前、右、后、左、下，六个面
    private static Face upFace;

    private static Face frontFace;

    private static Face rightFace;

    private static Face backFace;

    private static Face leftFace;

    private static Face downFace;

    private static Map<Integer, String> map = new HashMap<>();

    private String disruptedData;

    private String crossData;

    private String[] f2lData;

    private String ollData;

    private String pllData;

    private static Rubik rubik;

    private Rubik() {
        upFace = new Face();
        frontFace = new Face();
        rightFace = new Face();
        backFace = new Face();
        leftFace = new Face();
        downFace = new Face();
        map.put(1, "R");
        map.put(2, "L");
        map.put(3, "U");
        map.put(4, "D");
        map.put(5, "F");
        map.put(6, "B");
        map.put(7, "R'");
        map.put(8, "L'");
        map.put(9, "U'");
        map.put(10, "D'");
        map.put(11, "F'");
        map.put(12, "B'");
        map.put(13, "R2");
        map.put(14, "L2");
        map.put(15, "U2");
        map.put(16, "D2");
        map.put(17, "F2");
        map.put(18, "B2");
        map.put(19, "r");
        map.put(20, "l");
        map.put(21, "u");
        map.put(22, "d");
        map.put(23, "f");
        map.put(24, "b");
        map.put(31, "M");
        map.put(32, "E");
        map.put(33, "S");
        map.put(34, "x");
        map.put(35, "y");
        map.put(36, "z");
    }

    public synchronized static Rubik getInstance() {
        if (rubik == null) {
            rubik = new Rubik();
        }
        return rubik;
    }

    public Face getUpFace() {
        return upFace;
    }

    public Face getFrontFace() {
        return frontFace;
    }

    public Face getRightFace() {
        return rightFace;
    }

    public Face getBackFace() {
        return backFace;
    }

    public Face getLeftFace() {
        return leftFace;
    }

    public Face getDownFace() {
        return downFace;
    }

    public String getCrossData() {
        return crossData;
    }

    public String[] getF2lData() {
        return f2lData;
    }

    public String getOllData() {
        return ollData;
    }

    public String getPllData() {
        return pllData;
    }

    public void init() {
        upFace.init(Yellow);
        frontFace.init(Blue);
        rightFace.init(Red);
        backFace.init(Green);
        leftFace.init(Orange);
        downFace.init(White);
        crossStep = crossDepth = f2lIndex = 0;
        crossArray = new int[8];
        isCrossFinish = false;
        disruptedData = crossData = ollData = pllData = "";
        f2lData = new String[]{"", "", "", ""};
    }

    public String getDisruptedData(int step) { // 随机出一个打乱魔方的方式
        int lastTurn = -1;
        int currentTurn;
        for (int i = 0; i < step; i++) { // 打乱的方式
            do {
                currentTurn = (int) (Math.random() * 6);
            } while (currentTurn == lastTurn);
            disruptedData += "RLUDFB".charAt(currentTurn);
            disruptedData += " 2'".charAt((int) (Math.random() * 3));
            lastTurn = currentTurn;
        }
        return disruptedData;
    }

    public void handleTurn(String data) { // 解析传入的打乱方式
        if (data.contains("F2L_")) {
            data = data.substring(data.indexOf("F2L_") + 6, data.length());
        } else if (data.contains("OLL_")) {
            data = data.substring(data.indexOf("OLL_") + 6, data.length());
        } else if (data.contains("PLL_")) {
            data = data.substring(data.indexOf("PLL_") + 6, data.length());
        }
        for (int i = 0; i < data.length(); i += 2) {
            for (Map.Entry entry : map.entrySet()) {
                if (entry.getValue().equals(data.charAt(i) + "")) {
                    switch (data.charAt(i + 1)) {
                        case ' ':
                            turn((int) entry.getKey());
                            break;
                        case '2':
                            turn((int) entry.getKey());
                            turn((int) entry.getKey());
                            break;
                        case '\'':
                            turn((int) entry.getKey() + 6);
                            break;
                    }
                    break;
                }
            }
        }
    }

    private int crossStep;  // 循环的当前深度，0表示第一层循环，当depth++，进入下一层循环

    private int crossDepth; // 指定循环深度，0表示深度为0，即不进入下一层循环

    private boolean isCrossFinish; // Cross是否完成的标志

    private int[] crossArray; // 用于统计转动方式的数组，和turn方法结合起来用

    /**
     * CROSS Handle
     */
    public void cross() {
        while (!isCrossFinished()) { // 当Cross没结束，就开始计算Cross，step表示循环深度越来越深，
            judge();
            crossDepth++;
        }
    }

    private void judge() {
        for (int i = 1; i <= 18; i++) { // 一共12+6种方式转动
            turn(i); // 模拟转动一次
            crossArray[crossStep] = i; // 将转动的下标存入数组
            if (isCrossFinished()) { // 判断是否转成底层十字
                isCrossFinish = true;
                crossData = handleCrossArray(crossArray);
            } else if (crossStep < crossDepth) { // 不超过8个步骤能得出Cross,crossStep从0开始变化到7，深度由1至8
                crossStep++;
                judge(); // 调用自身，进入下一层循环
            }
            if (!isCrossFinish) {
                if (i > 12) {
                    turn(i);
                } else if (i < 7) { // 得出一次相反转动，将模拟转动复原
                    turn(i + 6);
                } else {
                    turn(i - 6);
                }
                if (i == 18) { // 当i==12并且循环Cross未完成，以免crossArray数据遭到破坏
                    crossArray[crossStep] = 0;
                    crossStep = crossStep > 0 ? crossStep - 1 : 0;
                }
            } else {
                break;
            }
        }
    }

    private String handleCrossArray(int[] crossArray) {
        String temp = "";
        for (int i = 0; i < crossArray.length && crossArray[i] != 0; i++) {
            temp += map.get(crossArray[i]);
            if (crossArray[i] < 7) {
                temp += " ";
            }
        }
        return temp;
    }

    private void turn(int turnWay) { // 用整型值turnWay来代表调用对应的方法
        switch (turnWay) {
            case 1:
                R();
                break;
            case 2:
                L();
                break;
            case 3:
                U();
                break;
            case 4:
                D();
                break;
            case 5:
                F();
                break;
            case 6:
                B();
                break;
            case 7:
                R_();
                break;
            case 8:
                L_();
                break;
            case 9:
                U_();
                break;
            case 10:
                D_();
                break;
            case 11:
                F_();
                break;
            case 12:
                B_();
                break;
            case 13:
                R();
                R();
                break;
            case 14:
                L();
                L();
                break;
            case 15:
                U();
                U();
                break;
            case 16:
                D();
                D();
                break;
            case 17:
                F();
                F();
                break;
            case 18:
                B();
                B();
                break;
            case 19:
                r();
                break;
            case 20:
                l();
                break;
            case 21:
                u();
                break;
            case 22:
                d();
                break;
            case 23:
                f();
                break;
            case 24:
                b();
                break;
            case 25:
                r_();
                break;
            case 26:
                l_();
                break;
            case 27:
                u_();
                break;
            case 28:
                d_();
                break;
            case 29:
                f_();
                break;
            case 30:
                b_();
                break;
            case 31:
                M();
                break;
            case 32:
                E();
                break;
            case 33:
                S();
                break;
            case 34:
                x();
                break;
            case 35:
                y();
                break;
            case 36:
                z();
                break;
            case 37:
                M_();
                break;
            case 38:
                E_();
                break;
            case 39:
                S_();
                break;
            case 40:
                x_();
                break;
            case 41:
                y_();
                break;
            case 42:
                z_();
                break;
            default:
                break;
        }
    }

    private boolean isCrossFinished() { // 当做成底层十字+侧边对齐，则Cross完成
        return downFace.getTwoStatus() == downFace.getFiveStatus()
                && downFace.getFourStatus() == downFace.getFiveStatus()
                && downFace.getSixStatus() == downFace.getFiveStatus()
                && downFace.getEightStatus() == downFace.getFiveStatus()
                && frontFace.getEightStatus() == frontFace.getFiveStatus()
                && leftFace.getEightStatus() == leftFace.getFiveStatus()
                && rightFace.getEightStatus() == rightFace.getFiveStatus()
                && backFace.getTwoStatus() == backFace.getFiveStatus();
    }

    /**
     * F2L Handle
     */
    private int f2lIndex;

    public void f2l() {
        for (; f2lIndex < 4; f2lIndex++) {
            if (!f2lSingleFinish()) { // 如果前、右层未完成F2L且do41F2L返回false(即未运行)
                if (!do41F2L()) {
                    judgeRubik(); // 如果没有遍历到公式，一定是三色块或两色块不在正确的位置，这一步调整色块
                    handleTurn(f2lData[f2lIndex]);
                    do41F2L();
                }
                handleTurn(f2lData[f2lIndex]);
            } else { // 如果当前棱已完成F2L则魔方整体右转
                handleTurn(f2lData[f2lIndex] = "y'");
            }
        }
    }

    // 分析三角块的位置，然后分析两角块的位置，进而能够触发公式
    // 这里写的很复杂是因为顾及到用尽量少的步数触发公式，如果要写简单，则先转出来一个角色块，再找棱色块出来(即两个步骤)
    private void judgeRubik() {
        // 如果三角在前右下角，这是特殊情况，包含公式f2l_01-f2l_11
        if (is3There(frontFace.getNineStatus(), rightFace.getSevenStatus(), downFace.getThreeStatus())) {
            if (frontFourFront()) { // 如果前层4号位为前层的颜色，左层6号位为右层颜色
                f2lData[f2lIndex] += "L'U2L ";
            } else if (frontFourRight()) {
                f2lData[f2lIndex] += "L'U'L ";
            } else if (backFourFront()) {
                f2lData[f2lIndex] += "L U2L'";
            } else if (backFourRight()) {
                f2lData[f2lIndex] += "L U'L'";
            } else if (backSixFront()) {
                f2lData[f2lIndex] += "R'U R U'";
            } else if (backSixRight()) {
                f2lData[f2lIndex] += "R'U R ";
            } else if (upTwoFront() || upSixRight()) {
                f2lData[f2lIndex] += "U ";
            } else if (upTwoRight() || upFourFront()) {
                f2lData[f2lIndex] += "U2";
            } else if (upFourRight() || upEightFront()) {
                f2lData[f2lIndex] += "U'";
            }
        } else if (is3There(frontFace.getSevenStatus(), leftFace.getNineStatus(), downFace.getOneStatus())) { // 如果三角在前左下角
            if (is2There(frontFace.getFourStatus(), leftFace.getSixStatus())) {
                f2lData[f2lIndex] += "L'U'L ";
            } else if (is2There(leftFace.getFourStatus(), backFace.getFourStatus())) {
                f2lData[f2lIndex] += "L U'L2U'L ";
            } else if (is2There(backFace.getSixStatus(), rightFace.getSixStatus())) {
                f2lData[f2lIndex] += "L'U'L R'U R ";
            } else if (is2There(backFace.getEightStatus(), upFace.getTwoStatus())) {
                f2lData[f2lIndex] += "U L'U'L ";
            } else {
                f2lData[f2lIndex] += "L'U'L ";
            }
        } else if (is3There(leftFace.getSevenStatus(), backFace.getOneStatus(), downFace.getSevenStatus())) { // 如果三角在后左下角
            if (is2There(frontFace.getFourStatus(), leftFace.getSixStatus())) {
                f2lData[f2lIndex] += "L U L2U L ";
            } else if (is2There(leftFace.getFourStatus(), backFace.getFourStatus())) {
                f2lData[f2lIndex] += "L U2L'";
            } else if (is2There(backFace.getSixStatus(), rightFace.getSixStatus())) {
                f2lData[f2lIndex] += "L U L'R'U R U'";
            } else if (is2There(upFace.getSixStatus(), rightFace.getTwoStatus())) {
                f2lData[f2lIndex] += "U L U2L'";
            } else {
                f2lData[f2lIndex] += "L U2L'";
            }
        } else if (is3There(rightFace.getNineStatus(), backFace.getThreeStatus(), downFace.getNineStatus())) { // 如果三角在后右下角
            if (is2There(frontFace.getFourStatus(), leftFace.getSixStatus())) {
                f2lData[f2lIndex] += "L'U'L R'U R U ";
            } else if (is2There(leftFace.getFourStatus(), backFace.getFourStatus())) {
                f2lData[f2lIndex] += "R'U'R L U'L'";
            } else if (is2There(backFace.getSixStatus(), rightFace.getSixStatus())) {
                f2lData[f2lIndex] += "R'U R U ";
            } else if (is2There(backFace.getEightStatus(), upFace.getTwoStatus())) {
                f2lData[f2lIndex] += "U'R'U R U ";
            } else {
                f2lData[f2lIndex] += "R'U R U ";
            }
        } else if (is3There(frontFace.getOneStatus(), upFace.getSevenStatus(), leftFace.getThreeStatus())) { // 如果三角在前左上角
            if (is2There(frontFace.getFourStatus(), leftFace.getSixStatus())) {
                f2lData[f2lIndex] += "L'U2L ";
            } else if (is2There(leftFace.getFourStatus(), backFace.getFourStatus())) {
                f2lData[f2lIndex] += "U L U'L'";
            } else if (is2There(backFace.getSixStatus(), rightFace.getSixStatus())) {
                f2lData[f2lIndex] += "R'U'R U ";
            } else {
                f2lData[f2lIndex] += "U'";
            }
        } else if (is3There(upFace.getOneStatus(), leftFace.getOneStatus(), backFace.getSevenStatus())) { // 如果三角在后左上角
            if (is2There(frontFace.getFourStatus(), leftFace.getSixStatus())) {
                f2lData[f2lIndex] += "U'L'U2L ";
            } else if (is2There(leftFace.getFourStatus(), backFace.getFourStatus())) {
                f2lData[f2lIndex] += "L U'L'";
            } else if (is2There(backFace.getSixStatus(), rightFace.getSixStatus())) {
                f2lData[f2lIndex] += "R'U'R U'";
            } else {
                f2lData[f2lIndex] += "U2";
            }
        } else if (is3There(rightFace.getThreeStatus(), backFace.getNineStatus(), upFace.getThreeStatus())) { // 如果三角在后右上角
            if (is2There(frontFace.getFourStatus(), leftFace.getSixStatus())) {
                f2lData[f2lIndex] += "L'U L ";
            } else if (is2There(leftFace.getFourStatus(), backFace.getFourStatus())) {
                f2lData[f2lIndex] += "L U L'";
            } else if (is2There(backFace.getSixStatus(), rightFace.getSixStatus())) {
                f2lData[f2lIndex] += "R'U R U'";
            } else {
                f2lData[f2lIndex] += "U ";
            }
        } else if (is3There(frontFace.getThreeStatus(), rightFace.getOneStatus(), upFace.getNineStatus())) { // 如果三角在前右上角
            if (is2There(frontFace.getFourStatus(), leftFace.getSixStatus())) {
                f2lData[f2lIndex] += "L'U'L U ";
            } else if (is2There(leftFace.getFourStatus(), backFace.getFourStatus())) {
                f2lData[f2lIndex] += "L U'L'U ";
            } else if (is2There(backFace.getSixStatus(), rightFace.getSixStatus())) {
                f2lData[f2lIndex] += "R'U R ";
            }
        }
    }

    private boolean f2lSingleFinish() {
        return frontFace.getSixStatus() == frontFace.getFiveStatus()
                && frontFace.getNineStatus() == frontFace.getFiveStatus()
                && rightFace.getFourStatus() == rightFace.getFiveStatus()
                && rightFace.getSevenStatus() == rightFace.getFiveStatus()
                && downFace.getThreeStatus() == downFace.getFiveStatus();
    }

    private boolean do41F2L() { //以下是41个F2L公式
        if (frontFace.getNineStatus() == frontFace.getFiveStatus()
                && rightFace.getSevenStatus() == rightFace.getFiveStatus()
                && downFace.getThreeStatus() == downFace.getFiveStatus()) {
            if (frontSixRight()) {
                f2lData[f2lIndex] += "F2L_01R U'R'U y'R'U2R U'U'R'U R ";
                return true;
            }
            if (upEightRight()) {
                f2lData[f2lIndex] += "F2L_02U R U'R'U'y'R'U R ";
                return true;
            }
            if (upSixFront()) {
                f2lData[f2lIndex] += "F2L_02R'F'R U R U'R'F y'";
                return true;
            }
        }
        if (frontFace.getNineStatus() == downFace.getFiveStatus()
                && rightFace.getSevenStatus() == frontFace.getFiveStatus()
                && downFace.getThreeStatus() == rightFace.getFiveStatus()) {
            if (frontSixFront()) {
                f2lData[f2lIndex] += "F2L_04R U R'U'R U2R'U'R U R'y'";
                return true;
            }
            if (frontSixRight()) {
                f2lData[f2lIndex] += "F2L_05R F U R U'R'F'U'R'y'";
                return true;
            }
            if (upEightRight()) {
                f2lData[f2lIndex] += "F2L_06y'R'U'R U R'U'R ";
                return true;
            }
            if (upSixFront()) {
                f2lData[f2lIndex] += "F2L_07R U'R'U R U'R'y'";
                return true;
            }
        }
        if (frontFace.getNineStatus() == rightFace.getFiveStatus()
                && rightFace.getSevenStatus() == downFace.getFiveStatus()
                && downFace.getThreeStatus() == frontFace.getFiveStatus()) {
            if (frontSixFront()) {
                f2lData[f2lIndex] += "F2L_08R U'R'U R U'U'R'U R U'R'y'";
                return true;
            }
            if (frontSixRight()) {
                f2lData[f2lIndex] += "F2L_09R U F R U R'U'F'R'y'";
                return true;
            }
            if (upEightRight()) {
                f2lData[f2lIndex] += "F2L_10y'R'U R U'R'U R ";
                return true;
            }
            if (upSixFront()) {
                f2lData[f2lIndex] += "F2L_11R U R'U'R U R'y'";
                return true;
            }
        }
        if (frontFace.getThreeStatus() == rightFace.getFiveStatus()
                && rightFace.getOneStatus() == frontFace.getFiveStatus()
                && upFace.getNineStatus() == downFace.getFiveStatus()) {
            if (frontSixFront()) {
                f2lData[f2lIndex] += "F2L_12R U R'U'R U R'U'R U R'y'";
                return true;
            }
            if (frontSixRight()) {
                f2lData[f2lIndex] += "F2L_13R U'R'U y'R'U R ";
                return true;
            }
            if (upEightRight()) {
                f2lData[f2lIndex] += "F2L_14y'R'U2R U R'U'R ";
                return true;
            }
            if (upFourRight()) {
                f2lData[f2lIndex] += "F2L_15y'U'R'U2R U'R'U R ";
                return true;
            }
            if (upTwoRight()) {
                f2lData[f2lIndex] += "F2L_16y'R'U R U'U'R'U'R ";
                return true;
            }
            if (upSixRight()) {
                f2lData[f2lIndex] += "F2L_17F U R U'R'F'R U'R'y'";
                return true;
            }
            if (upEightFront()) {
                f2lData[f2lIndex] += "F2L_18U R U'R'U'R U'R'U R U'R'y'";
                return true;
            }
            if (upFourFront()) {
                f2lData[f2lIndex] += "F2L_19R U'R'U2R U R'y'";
                return true;
            }
            if (upTwoFront()) {
                f2lData[f2lIndex] += "F2L_20U R U'U'R'U R U'R'y'";
                return true;
            }
            if (upSixFront()) {
                f2lData[f2lIndex] += "F2L_21R U'U'R'U'R U R'y'";
                return true;
            }
        }
        if (frontFace.getThreeStatus() == downFace.getFiveStatus()
                && rightFace.getOneStatus() == rightFace.getFiveStatus()
                && upFace.getNineStatus() == frontFace.getFiveStatus()) {
            if (frontSixFront()) {
                f2lData[f2lIndex] += "F2L_22U'R U'R'U2R U'R'y'";
                return true;
            }
            if (frontSixRight()) {
                f2lData[f2lIndex] += "F2L_23U'R U R'y'U R'U'R ";
                return true;
            }
            if (upEightRight()) {
                f2lData[f2lIndex] += "F2L_24y'U R'U R U'R'U'R ";
                return true;
            }
            if (upFourRight()) {
                f2lData[f2lIndex] += "F2L_25y'R'U'R ";
                return true;
            }
            if (upTwoRight()) {
                f2lData[f2lIndex] += "F2L_26y'U R'U'R U'R'U'R ";
                return true;
            }
            if (upSixRight()) {
                f2lData[f2lIndex] += "F2L_27y'R U'U'R'R'U'R2U'R'";
                return true;
            }
            if (upEightFront()) {
                f2lData[f2lIndex] += "F2L_28l U r U'r'U'l'y'";
                return true;
            }
            if (upFourFront()) {
                f2lData[f2lIndex] += "F2L_29U'R U'U'R'U2R U'R'y'";
                return true;
            }
            if (upTwoFront()) {
                f2lData[f2lIndex] += "F2L_30U'R U R'U'R U'U'R'y'";
                return true;
            }
            if (upSixFront()) {
                f2lData[f2lIndex] += "F2L_31U R U'R'y'";
                return true;
            }
        }
        if (frontFace.getThreeStatus() == frontFace.getFiveStatus()
                && rightFace.getOneStatus() == downFace.getFiveStatus()
                && upFace.getNineStatus() == rightFace.getFiveStatus()) {
            if (frontSixFront()) {
                f2lData[f2lIndex] += "F2L_32U'R U'U'R'U R U R'y'";
                return true;
            }
            if (frontSixRight()) {
                f2lData[f2lIndex] += "F2L_33y'U R'U'R d'R U R'y'";
                return true;
            }
            if (upEightRight()) {
                f2lData[f2lIndex] += "F2L_34y'U'R'U R ";
                return true;
            }
            if (upFourRight()) {
                f2lData[f2lIndex] += "F2L_35y'U R'U'R U'U'R'U R ";
                return true;
            }
            if (upTwoRight()) {
                f2lData[f2lIndex] += "F2L_36y'U R'U2R U'U'R'U R ";
                return true;
            }
            if (upSixRight()) {
                f2lData[f2lIndex] += "F2L_37R U'R'U2y'R'U'R ";
                return true;
            }
            if (upEightFront()) {
                f2lData[f2lIndex] += "F2L_38R'U2R2U R2U R y'";
                return true;
            }
            if (upFourFront()) {
                f2lData[f2lIndex] += "F2L_39U'R U R'U R U R'y'";
                return true;
            }
            if (upTwoFront()) {
                f2lData[f2lIndex] += "F2L_40R U R'y'";
                return true;
            }
            if (upSixFront()) {
                f2lData[f2lIndex] += "F2L_41U'R U'R'U R U R'y'";
                return true;
            }
        }
        return false;
    }

    /*以下一些公式是判断棱色块的位置与颜色，方便F2L公式的判断*/
    private boolean upTwoRight() { // 上层2号位的棱色块的颜色是右层颜色(后层8号位的颜色是前层颜色)
        return upFace.getTwoStatus() == rightFace.getFiveStatus()
                && backFace.getEightStatus() == frontFace.getFiveStatus();
    }

    private boolean upTwoFront() {
        return upFace.getTwoStatus() == frontFace.getFiveStatus()
                && backFace.getEightStatus() == rightFace.getFiveStatus();
    }

    private boolean upFourRight() {
        return upFace.getFourStatus() == rightFace.getFiveStatus()
                && leftFace.getTwoStatus() == frontFace.getFiveStatus();
    }

    private boolean upFourFront() {
        return upFace.getFourStatus() == frontFace.getFiveStatus()
                && leftFace.getTwoStatus() == rightFace.getFiveStatus();
    }

    private boolean upSixRight() {
        return upFace.getSixStatus() == rightFace.getFiveStatus()
                && rightFace.getTwoStatus() == frontFace.getFiveStatus();
    }

    private boolean upSixFront() {
        return upFace.getSixStatus() == frontFace.getFiveStatus()
                && rightFace.getTwoStatus() == rightFace.getFiveStatus();
    }

    private boolean upEightRight() {
        return upFace.getEightStatus() == rightFace.getFiveStatus()
                && frontFace.getTwoStatus() == frontFace.getFiveStatus();
    }

    private boolean upEightFront() {
        return upFace.getEightStatus() == frontFace.getFiveStatus()
                && frontFace.getTwoStatus() == rightFace.getFiveStatus();
    }

    private boolean frontFourRight() {
        return frontFace.getFourStatus() == rightFace.getFiveStatus()
                && leftFace.getSixStatus() == frontFace.getFiveStatus();
    }

    private boolean frontFourFront() {
        return frontFace.getFourStatus() == frontFace.getFiveStatus()
                && leftFace.getSixStatus() == rightFace.getFiveStatus();
    }

    private boolean frontSixRight() {
        return frontFace.getSixStatus() == rightFace.getFiveStatus()
                && rightFace.getFourStatus() == frontFace.getFiveStatus();
    }

    private boolean frontSixFront() {
        return frontFace.getSixStatus() == frontFace.getFiveStatus()
                && rightFace.getFourStatus() == rightFace.getFiveStatus();
    }

    private boolean backFourRight() {
        return backFace.getFourStatus() == rightFace.getFiveStatus()
                && leftFace.getFourStatus() == frontFace.getFiveStatus();
    }

    private boolean backFourFront() {
        return backFace.getFourStatus() == frontFace.getFiveStatus()
                && leftFace.getFourStatus() == rightFace.getFiveStatus();
    }

    private boolean backSixRight() {
        return backFace.getSixStatus() == rightFace.getFiveStatus()
                && rightFace.getSixStatus() == frontFace.getFiveStatus();
    }

    private boolean backSixFront() {
        return backFace.getSixStatus() == frontFace.getFiveStatus()
                && rightFace.getSixStatus() == rightFace.getFiveStatus();
    }

    // 用于判断某棱色块是否是前层与右层的棱色块，如红-蓝棱或蓝-红棱，返回true。
    private boolean is2There(int s1, int s2) {
        return isContains2(s1) && isContains2(s2);
    }

    private boolean is3There(int s1, int s2, int s3) {
        return isContains3(s1) && isContains3(s2) && isContains3(s3);
    }

    private boolean isContains2(int s) {
        return s == frontFace.getFiveStatus() || s == rightFace.getFiveStatus();
    }

    private boolean isContains3(int s) {
        return s == frontFace.getFiveStatus() || s == rightFace.getFiveStatus()
                || s == downFace.getFiveStatus();
    }

    /**
     * OLL Handle
     */
    public void oll() {
        if (!isFaceFinished(upFace)) {
            for (int i = 0; i < 4; i++) { // 循环四次，如果魔方正确则必然有一次OLL匹配到公式
                if (!run57OLL()) {
                    y_();
                    ollData += "y'";
                } else {
                    handleTurn(ollData);
                    break;
                }
            }
        }
    }

    private boolean run57OLL() { // 匹配OLL57个公式
        int b7 = backFace.getSevenStatus();
        int b8 = backFace.getEightStatus();
        int b9 = backFace.getNineStatus();
        int l1 = leftFace.getOneStatus();
        int l2 = leftFace.getTwoStatus();
        int l3 = leftFace.getThreeStatus();
        int r1 = rightFace.getOneStatus();
        int r2 = rightFace.getTwoStatus();
        int r3 = rightFace.getThreeStatus();
        int f1 = frontFace.getOneStatus();
        int f2 = frontFace.getTwoStatus();
        int f3 = frontFace.getThreeStatus();
        int u1 = upFace.getOneStatus();
        int u2 = upFace.getTwoStatus();
        int u3 = upFace.getThreeStatus();
        int u4 = upFace.getFourStatus();
        int u6 = upFace.getSixStatus();
        int u7 = upFace.getSevenStatus();
        int u8 = upFace.getEightStatus();
        int u9 = upFace.getNineStatus();
        if (judgeOLL(b8, l1, l2, l3, f2, r1, r2, r3)) {
            ollData += "OLL_01R U'U'R'R'F R F'U U R'F R F'";
            return true;
        }
        if (judgeOLL(b8, b9, l1, l2, l3, f2, f3, r2)) {
            ollData += "OLL_02F R U R'U'F'f R U R'U'f'";
            return true;
        }
        if (judgeOLL(b7, b8, u9, l2, l3, f2, r2, r3)) {
            ollData += "OLL_03f R U R'U'f'U'F R U R'U'F'";
            return true;
        }
        if (judgeOLL(b8, u3, l1, l2, f1, f2, r1, r2)) {
            ollData += "OLL_04f R U R'U'f'U F R U R'U'F'";
            return true;
        }
        if (judgeOLL(b7, b8, u6, u8, u9, l2, l3, r3)) {
            ollData += "OLL_05r'U2R U R'U r ";
            return true;
        }
        if (judgeOLL(u2, u3, u6, l1, l2, f1, f2, r1)) {
            ollData += "OLL_06r U'U'R'U'R U'r'";
            return true;
        }
        if (judgeOLL(b7, u2, u4, u7, f2, f3, r2, r2)) {
            ollData += "OLL_07r U R'U R U'U'r'";
            return true;
        }
        if (judgeOLL(b8, b9, u1, u4, u8, f1, r1, r2)) {
            ollData += "OLL_08r'U'R U'R'U2r ";
            return true;
        }
        if (judgeOLL(b9, u2, u4, u9, l1, f1, f2, r2)) {
            ollData += "OLL_09R U R'U'R'F R2U R'U'F'";
            return true;
        }
        if (judgeOLL(b7, b8, u3, u4, u8, l3, f3, r2)) {
            ollData += "OLL_10R U R'U R'F R F'R U'U'R'";
            return true;
        }
        if (judgeOLL(b7, b8, u6, u7, u8, l2, f3, r3)) {
            ollData += "OLL_11r'R2U R'U R U'U'R'U M'";
            return true;
        }
        if (judgeOLL(b9, u1, u2, u6, l2, f1, f2, r1)) {
            ollData += "OLL_12M'R'U'R U'R'U2R U'M ";
            return true;
        }
        if (judgeOLL(b7, b8, u3, u4, u6, l3, f2, f3)) {
            ollData += "OLL_13f R U R2U'R'U R U'f'";
            return true;
        }
        if (judgeOLL(b8, b9, u4, u6, u9, l1, f1, f2)) {
            ollData += "OLL_14R'F R U R'F'R F U'F'";
            return true;
        }
        if (judgeOLL(b7, b8, u4, u6, u9, l3, f2, r3)) {
            ollData += "OLL_15r'U'r R'U'R U r'U r ";
            return true;
        }
        if (judgeOLL(b8, u3, u4, u6, l1, f1, f2, r1)) {
            ollData += "OLL_16r U r'R U R'U'r U'r'";
            return true;
        }
        if (judgeOLL(b8, b9, u1, u9, l2, l3, f2, r2)) {
            ollData += "OLL_17l U'l'f R2B R'U R'U'f'";
            return true;
        }
        if (judgeOLL(b8, u1, u3, l2, f1, f2, f3, r2)) {
            ollData += "OLL_18r U R'U R U'U'r'r'U'R U'R'U2r ";
            return true;
        }
        if (judgeOLL(b8, u1, u3, l2, l3, f2, r1, r2)) {
            ollData += "OLL_19r'R U R U R'U'r R'R'F R F'";
            return true;
        }
        if (judgeOLL(b8, u1, u3, u7, u9, l2, f2, r2)) {
            ollData += "OLL_20r U R'U'M'M'U R U'R'U'M'";
            return true;
        }
        if (judgeOLL(b7, b9, u2, u4, u6, u8, f1, f3)) {
            ollData += "OLL_21R U'U'R'U'R U R'U'R U'R'";
            return true;
        }
        if (judgeOLL(b9, u2, u4, u6, u8, l1, l3, f3)) {
            ollData += "OLL_22R U'U'R'R'U'R2U'R'R'U'U'R ";
            return true;
        }
        if (judgeOLL(b7, b9, u2, u4, u6, u7, u8, u9)) {
            ollData += "OLL_23R2D'R U'U'R'D R U'U'R ";
            return true;
        }
        if (judgeOLL(b7, u2, u3, u4, u6, u8, u9, f1)) {
            ollData += "OLL_24r U R'U'r'F R F'";
            return true;
        }
        if (judgeOLL(u2, u3, u4, u6, u7, u8, l1, f3)) {
            ollData += "OLL_25F'r U R'U'r'F R ";
            return true;
        }
        if (judgeOLL(u2, u3, u4, u6, u8, l1, f1, r1)) {
            ollData += "OLL_26R U'U'R'U'R U'R'";
            return true;
        }
        if (judgeOLL(b7, u2, u4, u6, u7, u8, f3, r3)) {
            ollData += "OLL_27R U R'U R U'U'R'";
            return true;
        }
        if (judgeOLL(u1, u2, u3, u4, u7, u9, f2, r2)) {
            ollData += "OLL_28r U R'U'r'R U R U'R'";
            return true;
        }
        if (judgeOLL(b7, u2, u3, u4, u9, f1, f2, r2)) {
            ollData += "OLL_29R U R'U'R U'R'F'U'F R U R'";
            return true;
        }
        if (judgeOLL(b8, u1, u3, u6, u8, l2, l3, r1)) {
            ollData += "OLL_30f R U R2U'R'U R2U'R'f'";
            return true;
        }
        if (judgeOLL(b8, b9, u1, u4, u7, u8, f3, r2)) {
            ollData += "OLL_31r'F'U F r U'r'U'r ";
            return true;
        }
        if (judgeOLL(b7, b8, u3, u6, u8, u9, l2, f1)) {
            ollData += "OLL_32R U B'U'R'U R B R'";
            return true;
        }
        if (judgeOLL(b7, b8, u3, u4, u6, u9, f1, f2)) {
            ollData += "OLL_33R U R'U'R'F R F'";
            return true;
        }
        if (judgeOLL(b8, u4, u6, u7, u9, l1, f2, r3)) {
            ollData += "OLL_34R U R2U'R'F R U R U'F'";
            return true;
        }
        if (judgeOLL(b8, u1, u6, u8, u9, l2, f1, r3)) {
            ollData += "OLL_35R U'U'R'R'F R F'R U'U'R'";
            return true;
        }
        if (judgeOLL(b8, u1, u4, u8, u9, f1, r2, r3)) {
            ollData += "OLL_36R'U'R U'R'U R U l U'R'U x ";
            return true;
        }
        if (judgeOLL(u1, u2, u4, u9, f1, f2, r2, r3)) {
            ollData += "OLL_37F R U'R'U'R U R'F'";
            return true;
        }
        if (judgeOLL(b7, u2, u3, u4, u7, f2, r1, r2)) {
            ollData += "OLL_38R U R'U R U'R'U'R'F R F'";
            return true;
        }
        if (judgeOLL(b8, u3, u4, u6, u7, l1, f2, f3)) {
            ollData += "OLL_39R U R'F'U'F U R U2R'";
            return true;
        }
        if (judgeOLL(b8, b9, u1, u4, u6, u9, l3, f2)) {
            ollData += "OLL_40R'F R U R'U'F'U R ";
            return true;
        }
        if (judgeOLL(b7, b9, u2, u4, u7, u9, f2, r2)) {
            ollData += "OLL_41R U R'U R U'U'R'F R U R'U'F'";
            return true;
        }
        if (judgeOLL(b8, u1, u3, u4, u8, f1, f3, r2)) {
            ollData += "OLL_42R'U'R U'R'U2R F R U R'U'F'";
            return true;
        }
        if (judgeOLL(b8, u1, u4, u7, u8, r1, r2, r3)) {
            ollData += "OLL_43B'U'R'U R B ";
            return true;
        }
        if (judgeOLL(b8, u3, u6, u8, u9, l1, l2, l3)) {
            ollData += "OLL_44f R U R'U'f'";
            return true;
        }
        if (judgeOLL(b8, u3, u4, u6, u9, l1, l3, f2)) {
            ollData += "OLL_45F R U R'U'F'";
            return true;
        }
        if (judgeOLL(u1, u2, u7, u8, l2, r1, r2, r3)) {
            ollData += "OLL_46R'U'R'F R F'U R ";
            return true;
        }
        if (judgeOLL(b7, u2, u6, l2, f1, f2, r1, r3)) {
            ollData += "OLL_47b'U'R'U R U'R'U R b ";
            return true;
        }
        if (judgeOLL(b9, u2, u4, l1, l3, f2, f3, r2)) {
            ollData += "OLL_48F R U R'U'R U R'U'F'";
            return true;
        }
        if (judgeOLL(b7, b8, u4, u8, f1, r1, r2, r3)) {
            ollData += "OLL_49R B'R'R'F R2B R'R'F'R ";
            return true;
        }
        if (judgeOLL(b8, b9, u6, u8, l1, l2, l3, f3)) {
            ollData += "OLL_50r'U r2U'r'r'U'r2U r'";
            return true;
        }
        if (judgeOLL(b8, b9, u4, u6, l1, l3, f2, f3)) {
            ollData += "OLL_51f R U R'U'R U R'U'f'";
            return true;
        }
        if (judgeOLL(b9, u2, u8, l1, l2, l3, f3, r2)) {
            ollData += "OLL_52R'F'U'F U'R U R'U R ";
            return true;
        }
        if (judgeOLL(b7, b8, b9, u4, u8, f1, f3, r2)) {
            ollData += "OLL_53r'U2R U R'U'R U R'U r ";
            return true;
        }
        if (judgeOLL(b7, b9, u2, u4, f1, f2, f3, r2)) {
            ollData += "OLL_54r U'U'R'U'R U R'U'R U'r'";
            return true;
        }
        if (judgeOLL(b7, b8, b9, u4, u6, f1, f2, f3)) {
            ollData += "OLL_55r U'U'R'U'r'R2U R'U'r U'r'";
            return true;
        }
        if (judgeOLL(b8, u4, u6, l1, l3, f2, r1, r3)) {
            ollData += "OLL_56r U r'U R U'R'U R U'R'r U'r'";
            return true;
        }
        if (judgeOLL(b8, u1, u3, u4, u6, u7, u9, f2)) {
            ollData += "OLL_57R U R'U'M'U R U'r'";
            return true;
        }
        return false;
    }

    // 判断属于哪一种OLL的情况，传入参数表示对应了顶层颜色的颜色位置
    private boolean judgeOLL(int c1, int c2, int c3, int c4, int c5, int c6, int c7, int c8) {
        int u5 = upFace.getFiveStatus();
        return c1 == u5 && c2 == u5 && c3 == u5 && c4 == u5
                && c5 == u5 && c6 == u5 && c7 == u5 && c8 == u5;
    }

    /**
     * PLL Handle
     */
    public void pll() {
        if (!isPLLFinished()) {
            for (int i = 0; i < 4; i++) {
                if (!run21PLL()) {
                    y_(); // 如果i==3，执行到这一步，那就是魔方不能还原
                    pllData += "y'";
                } else {
                    handleTurn(pllData);
                    break;
                }
            }
        }
        // PLL结束后，判断颜色对应最后的U步骤
        if (rightFace.getOneStatus() == frontFace.getFiveStatus()) {
            U();
            pllData += "  U ";
        } else if (leftFace.getOneStatus() == frontFace.getFiveStatus()) {
            U_();
            pllData += "  U'";
        } else if (backFace.getNineStatus() == frontFace.getFiveStatus()) {
            U();
            U();
            pllData += "  U2";
        }
    }

    private boolean isPLLFinished() { // 只要有两层对好了，那就是PLL对好了
        return leftFace.getOneStatus() == leftFace.getTwoStatus()
                && leftFace.getOneStatus() == leftFace.getThreeStatus()
                && frontFace.getOneStatus() == frontFace.getTwoStatus()
                && frontFace.getOneStatus() == frontFace.getThreeStatus();
    }

    private boolean run21PLL() {
        int b7 = backFace.getSevenStatus();
        int b8 = backFace.getEightStatus();
        int b9 = backFace.getNineStatus();
        int l1 = leftFace.getOneStatus();
        int l2 = leftFace.getTwoStatus();
        int l3 = leftFace.getThreeStatus();
        int r1 = rightFace.getOneStatus();
        int r2 = rightFace.getTwoStatus();
        int r3 = rightFace.getThreeStatus();
        int f1 = frontFace.getOneStatus();
        int f2 = frontFace.getTwoStatus();
        int f3 = frontFace.getThreeStatus();
        if (r2 == l1 && l2 == f1 && f2 == r1
                && is3Equals(b7, b8, b9) && is2Equals(l1, l3, l2)
                && is2Equals(r1, r3, r2) && is2Equals(f1, f3, f2)) {
            pllData += "PLL_01M'M'U M U2M'U M'M'";
            return true;
        }
        if (r2 == f1 && f2 == l1 && l2 == r1
                && is3Equals(b7, b8, b9) && is2Equals(l1, l3, l2)
                && is2Equals(r1, r3, r2) && is2Equals(f1, f3, f2)) {
            pllData += "PLL_02M'M'U'M U2M'U'M'M'";
            return true;
        }
        if (b8 == f1 && f2 == b7 && l2 == r1 && r2 == l1
                && is2Equals(b7, b9, b8) && is2Equals(l1, l3, l2)
                && is2Equals(r1, r3, r2) && is2Equals(f1, f3, f2)) {
            pllData += "PLL_03M'M'U M'M'U U M'M'U M'M'";
            return true;
        }
        if (b8 == r1 && r2 == b7 && l2 == f1 && f2 == l1
                && is2Equals(b7, b9, b8) && is2Equals(l1, l3, l2)
                && is2Equals(r1, r3, r2) && is2Equals(f1, f3, f2)) {
            pllData += "PLL_04M'U M'M'U M'M'U M'U U M'M'U'";
            return true;
        }
        if (r3 == f2 && b9 == r2 && f3 == l2 && r1 == f2
                && l3 == r2 && f1 == b8 && is2Equals(l1, l2, l3)
                && is2Equals(b7, b8, b9)) {
            pllData += "PLL_05x'R R D D R'U'R D D R'U R'x ";
            return true;
        }
        if (b9 == f2 && r3 == l2 && l3 == f2 && f1 == r2
                && f3 == r2 && r1 == b8 && is2Equals(l1, l2, l3)
                && is2Equals(b7, b8, b9)) {
            pllData += "PLL_06x'R U'R D D R'U R D D R'R'x ";
            return true;
        }
        if (f3 == r2 && r1 == b8 && r3 == f2 && b9 == r2
                && f1 == l2 && l3 == b8 && l1 == f2 && b7 == l2) {
            pllData += "PLL_07x'R U'R'D R U R'D'R U R'D R U'R'D'x ";
            return true;
        }
        if (is2Equals(l1, l3, l2) && is2Equals(b7, b8, b9) && is2Equals(f1, f2, f3)
                && r2 == l1 && r1 == b8 && r3 == f2
                && l2 == f3 && l2 == b9) {
            pllData += "PLL_08R U R'U'R'F R2U'R'U'R U R'F'";
            return true;
        }
        if (is3Equals(l1, l2, l3) && f2 == b7 && b8 == f1 && f3 == r2
                && r1 == b7 && r3 == f1 && b9 == r2) {
            pllData += "PLL_09R'U'F'R U R'U'R'F R2U'R'U'R U R'U R ";
            return true;
        }
        if (is2Equals(l2, l3, l1) && is2Equals(f1, f2, f3)
                && f3 == b9 && r1 == l2 && l1 == r3 && b7 == f2
                && r2 == b9 && b8 == r3) {
            pllData += "PLL_10R'U R'd'R'F'R2U'R'U R'F R F ";
            return true;
        }
        if (is2Equals(f1, f2, f3) && is2Equals(r2, r3, r1)
                && f3 == b9 && r1 == l3 && l2 == b9 && b8 == l3) {
            pllData += "PLL_11F R U'R'U'R U R'F'R U R'U'R'F R F'";
            return true;
        }
        if (is3Equals(b7, b8, b9) && is2Equals(l1, l2, l3)
                && r2 == r1 && r2 == l3
                && f1 == r3 && f2 == r3 && f3 == l2) {
            pllData += "PLL_12z U'R D'R2U R'U'R2U D R'z'";
            return true;
        }
        if (is3Equals(l1, l2, l3) && is2Equals(b7, b8, b9)
                && f2 == f3 && f2 == b9
                && r1 == b8 && r2 == f1 && r3 == f1) {
            pllData += "PLL_13R U R'F'R U R'U'R'F R2U'R'U'";
            return true;
        }
        if (is2Equals(l2, l3, l1) && f2 == r1 && r2 == f1
                && b9 == l2 && r3 == b8
                && l1 == b8 && b7 == r1) {
            pllData += "PLL_14R'U2R U'U'R'F R U R'U'R'F'R2U'";
            return true;
        }
        if (is2Equals(f1, f2, f3) && l2 == b7 && b8 == l1
                && b9 == r2 && r3 == f2
                && r1 == b7 && f3 == r2) {
            pllData += "PLL_15R U'R'U'R U R D R'U'R D'R'U2R'U'";
            return true;
        }
        if (is2Equals(l1, l3, l2) && is2Equals(b8, b9, b7)
                && is3NotEquals(f1, f2, f3) && is3NotEquals(r1, r2, r3)) {
            pllData += "PLL_16R'R'u'R U'R U R'u R2f R'f'";
            return true;
        }
        if (is2Equals(l1, l3, l2) && is2Equals(r1, r2, r3)
                && is3NotEquals(b7, b8, b9) && is3NotEquals(f1, f2, f3)) {
            pllData += "PLL_17R U R'y'R2u'R U'R'U R'u R2";
            return true;
        }
        if (is2Equals(l1, l3, l2) && is2Equals(f2, f3, f1)
                && is3NotEquals(b7, b8, b9) && is3NotEquals(r1, r2, r3)) {
            pllData += "PLL_18R2u R'U R'U'R u'R'R'F'U F ";
            return true;
        }
        if (is2Equals(l1, l3, l2) && is2Equals(r2, r3, r1)
                && is3NotEquals(f1, f2, f3) && is3NotEquals(b7, b8, b9)) {
            pllData += "PLL_19R'd'F R2u R'U R U'R u'R'R'";
            return true;
        }
        if (is2Equals(f1, f2, f3) && is2Equals(b8, b9, b7)
                && l2 == r3 && r2 == l3
                && b7 == f2 && l1 == r3 && f3 == b8 && r1 == l3) {
            pllData += "PLL_20R'U R U'R'F'U'F R U R'F R'F'R U'R ";
            return true;
        }
        if (is2Equals(b7, b8, b9) && is2Equals(f2, f3, f1)
                && l2 == r1 && r2 == l1
                && l3 == r1 && f1 == b8 && r3 == l1 && b9 == f2) {
            pllData += "PLL_21R U R'U R U R'F'R U R'U'R'F R2U'R'U2R U'R'";
            return true;
        }
        return false;
    }

    private boolean is2Equals(int c1, int c2, int c3) { // 前两个颜色相等且与第三个不相等
        return c1 == c2 && c1 != c3;
    }

    private boolean is3Equals(int c1, int c2, int c3) { // 三个颜色都相等
        return c1 == c2 && c2 == c3;
    }

    private boolean is3NotEquals(int c1, int c2, int c3) { // 三个颜色都不相等
        return c1 != c2 && c2 != c3 && c1 != c3;
    }

    /**
     * 以下部分为转换魔方的单个指令
     */

    // 右边两层右转 右2右 r 
    private void r() {
        R();
        M_();
    }

    // 右边两层左转 右2左 r'
    private void r_() {
        R_();
        M();
    }

    // 左边两层右转 左2右 l
    private void l() {
        L();
        M();
    }

    // 左边两层左转 左2左 l'
    private void l_() {
        L_();
        M_();
    }

    // 上面两层左转 上2左 u
    private void u() {
        U();
        E_();
    }

    // 上面两层右转 上2右 u'
    private void u_() {
        U_();
        E();
    }

    // 下面两层右转 下2右 d 
    private void d() {
        E();
        D();
    }

    // 下面两层左转 下2左 d'
    private void d_() {
        E_();
        D_();
    }

    // 前面两层右转 前2右 f 
    private void f() {
        F();
        S();
    }

    // 前面两层左转 前2左 f'
    private void f_() {
        F_();
        S_();
    }

    // 后面两层左转 后2左 b 
    private void b() {
        B();
        S_();
    }

    // 后面两层右转 后2右 b'
    private void b_() {
        B_();
        S();
    }

    // 绕x轴向上(左到右为x轴) 前层整体上转 x 
    private void x() {
        L_();
        R();
        M_();
    }

    // 绕x轴向下 前层整体下转 x'
    private void x_() {
        L();
        R_();
        M();
    }

    // 绕y轴向左(上到下为y轴) 前层整体左转 y
    private void y() {
        U();
        D_();
        E_();
    }

    // 绕y轴向右 前层整体右转 y'
    private void y_() {
        U_();
        D();
        E();
    }

    // 绕z轴向左(前到后为z轴) 上层整体左转 z'
    private void z_() {
        B();
        F_();
        S_();
    }

    // 绕z轴向右(前到后为z轴) 上层整体右转 z
    private void z() {
        B_();
        F();
        S();
    }

    // 前层中层上转
    private void M_() {
        int tempOne = frontFace.getTwoStatus();
        int tempTwo = frontFace.getFiveStatus();
        int tempThree = frontFace.getEightStatus();
        frontFace.setTwoStatus(downFace.getTwoStatus());
        frontFace.setFiveStatus(downFace.getFiveStatus());
        frontFace.setEightStatus(downFace.getEightStatus());
        downFace.setTwoStatus(backFace.getTwoStatus());
        downFace.setFiveStatus(backFace.getFiveStatus());
        downFace.setEightStatus(backFace.getEightStatus());
        backFace.setTwoStatus(upFace.getTwoStatus());
        backFace.setFiveStatus(upFace.getFiveStatus());
        backFace.setEightStatus(upFace.getEightStatus());
        upFace.setTwoStatus(tempOne);
        upFace.setFiveStatus(tempTwo);
        upFace.setEightStatus(tempThree);
    }

    // 前层中层下转
    private void M() {
        int tempOne = frontFace.getTwoStatus();
        int tempTwo = frontFace.getFiveStatus();
        int tempThree = frontFace.getEightStatus();
        frontFace.setTwoStatus(upFace.getTwoStatus());
        frontFace.setFiveStatus(upFace.getFiveStatus());
        frontFace.setEightStatus(upFace.getEightStatus());
        upFace.setTwoStatus(backFace.getTwoStatus());
        upFace.setFiveStatus(backFace.getFiveStatus());
        upFace.setEightStatus(backFace.getEightStatus());
        backFace.setTwoStatus(downFace.getTwoStatus());
        backFace.setFiveStatus(downFace.getFiveStatus());
        backFace.setEightStatus(downFace.getEightStatus());
        downFace.setTwoStatus(tempOne);
        downFace.setFiveStatus(tempTwo);
        downFace.setEightStatus(tempThree);
    }

    // 前层中层左转
    private void E_() {
        int tempOne = frontFace.getFourStatus();
        int tempTwo = frontFace.getFiveStatus();
        int tempThree = frontFace.getSixStatus();
        frontFace.setFourStatus(rightFace.getFourStatus());
        frontFace.setFiveStatus(rightFace.getFiveStatus());
        frontFace.setSixStatus(rightFace.getSixStatus());
        rightFace.setFourStatus(backFace.getSixStatus());
        rightFace.setFiveStatus(backFace.getFiveStatus());
        rightFace.setSixStatus(backFace.getFourStatus());
        backFace.setSixStatus(leftFace.getFourStatus());
        backFace.setFiveStatus(leftFace.getFiveStatus());
        backFace.setFourStatus(leftFace.getSixStatus());
        leftFace.setFourStatus(tempOne);
        leftFace.setFiveStatus(tempTwo);
        leftFace.setSixStatus(tempThree);
    }

    // 前层中层右转
    private void E() {
        int tempOne = frontFace.getFourStatus();
        int tempTwo = frontFace.getFiveStatus();
        int tempThree = frontFace.getSixStatus();
        frontFace.setFourStatus(leftFace.getFourStatus());
        frontFace.setFiveStatus(leftFace.getFiveStatus());
        frontFace.setSixStatus(leftFace.getSixStatus());
        leftFace.setFourStatus(backFace.getSixStatus());
        leftFace.setFiveStatus(backFace.getFiveStatus());
        leftFace.setSixStatus(backFace.getFourStatus());
        backFace.setSixStatus(rightFace.getFourStatus());
        backFace.setFiveStatus(rightFace.getFiveStatus());
        backFace.setFourStatus(rightFace.getSixStatus());
        rightFace.setFourStatus(tempOne);
        rightFace.setFiveStatus(tempTwo);
        rightFace.setSixStatus(tempThree);
    }

    // 上层中层左转
    private void S_() {
        int tempOne = upFace.getFourStatus();
        int tempTwo = upFace.getFiveStatus();
        int tempThree = upFace.getSixStatus();
        upFace.setFourStatus(rightFace.getTwoStatus());
        upFace.setFiveStatus(rightFace.getFiveStatus());
        upFace.setSixStatus(rightFace.getEightStatus());
        rightFace.setTwoStatus(downFace.getSixStatus());
        rightFace.setFiveStatus(downFace.getFiveStatus());
        rightFace.setEightStatus(downFace.getFourStatus());
        downFace.setSixStatus(leftFace.getEightStatus());
        downFace.setFiveStatus(leftFace.getFiveStatus());
        downFace.setFourStatus(leftFace.getTwoStatus());
        leftFace.setEightStatus(tempOne);
        leftFace.setFiveStatus(tempTwo);
        leftFace.setTwoStatus(tempThree);
    }

    // 上层中层右转
    private void S() {
        int tempOne = upFace.getFourStatus();
        int tempTwo = upFace.getFiveStatus();
        int tempThree = upFace.getSixStatus();
        upFace.setFourStatus(leftFace.getEightStatus());
        upFace.setFiveStatus(leftFace.getFiveStatus());
        upFace.setSixStatus(leftFace.getTwoStatus());
        leftFace.setEightStatus(downFace.getSixStatus());
        leftFace.setFiveStatus(downFace.getFiveStatus());
        leftFace.setTwoStatus(downFace.getFourStatus());
        downFace.setSixStatus(rightFace.getTwoStatus());
        downFace.setFiveStatus(rightFace.getFiveStatus());
        downFace.setFourStatus(rightFace.getEightStatus());
        rightFace.setTwoStatus(tempOne);
        rightFace.setFiveStatus(tempTwo);
        rightFace.setEightStatus(tempThree);
    }

    // 上层左转
    private void U() {
        faceToRight(upFace);
        int tempOne = frontFace.getOneStatus();
        int tempTwo = frontFace.getTwoStatus();
        int tempThree = frontFace.getThreeStatus();
        frontFace.setOneStatus(rightFace.getOneStatus());
        frontFace.setTwoStatus(rightFace.getTwoStatus());
        frontFace.setThreeStatus(rightFace.getThreeStatus());
        rightFace.setOneStatus(backFace.getNineStatus());
        rightFace.setTwoStatus(backFace.getEightStatus());
        rightFace.setThreeStatus(backFace.getSevenStatus());
        backFace.setNineStatus(leftFace.getOneStatus());
        backFace.setEightStatus(leftFace.getTwoStatus());
        backFace.setSevenStatus(leftFace.getThreeStatus());
        leftFace.setOneStatus(tempOne);
        leftFace.setTwoStatus(tempTwo);
        leftFace.setThreeStatus(tempThree);
    }

    // 上层右转
    private void U_() {
        faceToLeft(upFace);
        int tempOne = frontFace.getOneStatus();
        int tempTwo = frontFace.getTwoStatus();
        int tempThree = frontFace.getThreeStatus();
        frontFace.setOneStatus(leftFace.getOneStatus());
        frontFace.setTwoStatus(leftFace.getTwoStatus());
        frontFace.setThreeStatus(leftFace.getThreeStatus());
        leftFace.setOneStatus(backFace.getNineStatus());
        leftFace.setTwoStatus(backFace.getEightStatus());
        leftFace.setThreeStatus(backFace.getSevenStatus());
        backFace.setNineStatus(rightFace.getOneStatus());
        backFace.setEightStatus(rightFace.getTwoStatus());
        backFace.setSevenStatus(rightFace.getThreeStatus());
        rightFace.setOneStatus(tempOne);
        rightFace.setTwoStatus(tempTwo);
        rightFace.setThreeStatus(tempThree);
    }

    // 下层左转
    private void D_() {
        faceToLeft(downFace);
        int tempOne = frontFace.getSevenStatus();
        int tempTwo = frontFace.getEightStatus();
        int tempThree = frontFace.getNineStatus();
        frontFace.setSevenStatus(rightFace.getSevenStatus());
        frontFace.setEightStatus(rightFace.getEightStatus());
        frontFace.setNineStatus(rightFace.getNineStatus());
        rightFace.setSevenStatus(backFace.getThreeStatus());
        rightFace.setEightStatus(backFace.getTwoStatus());
        rightFace.setNineStatus(backFace.getOneStatus());
        backFace.setThreeStatus(leftFace.getSevenStatus());
        backFace.setTwoStatus(leftFace.getEightStatus());
        backFace.setOneStatus(leftFace.getNineStatus());
        leftFace.setSevenStatus(tempOne);
        leftFace.setEightStatus(tempTwo);
        leftFace.setNineStatus(tempThree);
    }

    // 下层右转
    private void D() {
        faceToRight(downFace);
        int tempOne = frontFace.getSevenStatus();
        int tempTwo = frontFace.getEightStatus();
        int tempThree = frontFace.getNineStatus();
        frontFace.setSevenStatus(leftFace.getSevenStatus());
        frontFace.setEightStatus(leftFace.getEightStatus());
        frontFace.setNineStatus(leftFace.getNineStatus());
        leftFace.setSevenStatus(backFace.getThreeStatus());
        leftFace.setEightStatus(backFace.getTwoStatus());
        leftFace.setNineStatus(backFace.getOneStatus());
        backFace.setThreeStatus(rightFace.getSevenStatus());
        backFace.setTwoStatus(rightFace.getEightStatus());
        backFace.setOneStatus(rightFace.getNineStatus());
        rightFace.setSevenStatus(tempOne);
        rightFace.setEightStatus(tempTwo);
        rightFace.setNineStatus(tempThree);
    }

    // 左层左转
    private void L_() {
        faceToLeft(leftFace);
        int tempOne = upFace.getOneStatus();
        int tempTwo = upFace.getFourStatus();
        int tempThree = upFace.getSevenStatus();
        upFace.setOneStatus(frontFace.getOneStatus());
        upFace.setFourStatus(frontFace.getFourStatus());
        upFace.setSevenStatus(frontFace.getSevenStatus());
        frontFace.setOneStatus(downFace.getOneStatus());
        frontFace.setFourStatus(downFace.getFourStatus());
        frontFace.setSevenStatus(downFace.getSevenStatus());
        downFace.setOneStatus(backFace.getOneStatus());
        downFace.setFourStatus(backFace.getFourStatus());
        downFace.setSevenStatus(backFace.getSevenStatus());
        backFace.setOneStatus(tempOne);
        backFace.setFourStatus(tempTwo);
        backFace.setSevenStatus(tempThree);
    }

    // 左层右转
    private void L() {
        faceToRight(leftFace);
        int tempOne = upFace.getOneStatus();
        int tempTwo = upFace.getFourStatus();
        int tempThree = upFace.getSevenStatus();
        upFace.setOneStatus(backFace.getOneStatus());
        upFace.setFourStatus(backFace.getFourStatus());
        upFace.setSevenStatus(backFace.getSevenStatus());
        backFace.setOneStatus(downFace.getOneStatus());
        backFace.setFourStatus(downFace.getFourStatus());
        backFace.setSevenStatus(downFace.getSevenStatus());
        downFace.setOneStatus(frontFace.getOneStatus());
        downFace.setFourStatus(frontFace.getFourStatus());
        downFace.setSevenStatus(frontFace.getSevenStatus());
        frontFace.setOneStatus(tempOne);
        frontFace.setFourStatus(tempTwo);
        frontFace.setSevenStatus(tempThree);
    }

    // 右层左转
    private void R_() {
        faceToLeft(rightFace);
        int tempOne = upFace.getNineStatus();
        int tempTwo = upFace.getSixStatus();
        int tempThree = upFace.getThreeStatus();
        upFace.setNineStatus(backFace.getNineStatus());
        upFace.setSixStatus(backFace.getSixStatus());
        upFace.setThreeStatus(backFace.getThreeStatus());
        backFace.setNineStatus(downFace.getNineStatus());
        backFace.setSixStatus(downFace.getSixStatus());
        backFace.setThreeStatus(downFace.getThreeStatus());
        downFace.setNineStatus(frontFace.getNineStatus());
        downFace.setSixStatus(frontFace.getSixStatus());
        downFace.setThreeStatus(frontFace.getThreeStatus());
        frontFace.setNineStatus(tempOne);
        frontFace.setSixStatus(tempTwo);
        frontFace.setThreeStatus(tempThree);
    }

    // 右层右转
    private void R() {
        faceToRight(rightFace);
        int tempOne = upFace.getThreeStatus();
        int tempTwo = upFace.getSixStatus();
        int tempThree = upFace.getNineStatus();
        upFace.setThreeStatus(frontFace.getThreeStatus());
        upFace.setSixStatus(frontFace.getSixStatus());
        upFace.setNineStatus(frontFace.getNineStatus());
        frontFace.setThreeStatus(downFace.getThreeStatus());
        frontFace.setSixStatus(downFace.getSixStatus());
        frontFace.setNineStatus(downFace.getNineStatus());
        downFace.setThreeStatus(backFace.getThreeStatus());
        downFace.setSixStatus(backFace.getSixStatus());
        downFace.setNineStatus(backFace.getNineStatus());
        backFace.setThreeStatus(tempOne);
        backFace.setSixStatus(tempTwo);
        backFace.setNineStatus(tempThree);
    }

    // 前层左转
    private void F_() {
        faceToLeft(frontFace);
        int tempOne = upFace.getSevenStatus();
        int tempTwo = upFace.getEightStatus();
        int tempThree = upFace.getNineStatus();
        upFace.setSevenStatus(rightFace.getOneStatus());
        upFace.setEightStatus(rightFace.getFourStatus());
        upFace.setNineStatus(rightFace.getSevenStatus());
        rightFace.setOneStatus(downFace.getThreeStatus());
        rightFace.setFourStatus(downFace.getTwoStatus());
        rightFace.setSevenStatus(downFace.getOneStatus());
        downFace.setThreeStatus(leftFace.getNineStatus());
        downFace.setTwoStatus(leftFace.getSixStatus());
        downFace.setOneStatus(leftFace.getThreeStatus());
        leftFace.setNineStatus(tempOne);
        leftFace.setSixStatus(tempTwo);
        leftFace.setThreeStatus(tempThree);
    }

    // 前层右转
    private void F() {
        faceToRight(frontFace);
        int tempOne = upFace.getSevenStatus();
        int tempTwo = upFace.getEightStatus();
        int tempThree = upFace.getNineStatus();
        upFace.setSevenStatus(leftFace.getNineStatus());
        upFace.setEightStatus(leftFace.getSixStatus());
        upFace.setNineStatus(leftFace.getThreeStatus());
        leftFace.setNineStatus(downFace.getThreeStatus());
        leftFace.setSixStatus(downFace.getTwoStatus());
        leftFace.setThreeStatus(downFace.getOneStatus());
        downFace.setThreeStatus(rightFace.getOneStatus());
        downFace.setTwoStatus(rightFace.getFourStatus());
        downFace.setOneStatus(rightFace.getSevenStatus());
        rightFace.setOneStatus(tempOne);
        rightFace.setFourStatus(tempTwo);
        rightFace.setSevenStatus(tempThree);
    }

    // 后层左转
    private void B() {
        faceToRight(backFace);
        int tempOne = upFace.getOneStatus();
        int tempTwo = upFace.getTwoStatus();
        int tempThree = upFace.getThreeStatus();
        upFace.setOneStatus(rightFace.getThreeStatus());
        upFace.setTwoStatus(rightFace.getSixStatus());
        upFace.setThreeStatus(rightFace.getNineStatus());
        rightFace.setThreeStatus(downFace.getNineStatus());
        rightFace.setSixStatus(downFace.getEightStatus());
        rightFace.setNineStatus(downFace.getSevenStatus());
        downFace.setNineStatus(leftFace.getSevenStatus());
        downFace.setEightStatus(leftFace.getFourStatus());
        downFace.setSevenStatus(leftFace.getOneStatus());
        leftFace.setSevenStatus(tempOne);
        leftFace.setFourStatus(tempTwo);
        leftFace.setOneStatus(tempThree);
    }

    // 后层右转
    private void B_() {
        faceToLeft(backFace);
        int tempOne = upFace.getOneStatus();
        int tempTwo = upFace.getTwoStatus();
        int tempThree = upFace.getThreeStatus();
        upFace.setOneStatus(leftFace.getSevenStatus());
        upFace.setTwoStatus(leftFace.getFourStatus());
        upFace.setThreeStatus(leftFace.getOneStatus());
        leftFace.setSevenStatus(downFace.getNineStatus());
        leftFace.setFourStatus(downFace.getEightStatus());
        leftFace.setOneStatus(downFace.getSevenStatus());
        downFace.setNineStatus(rightFace.getThreeStatus());
        downFace.setEightStatus(rightFace.getSixStatus());
        downFace.setSevenStatus(rightFace.getNineStatus());
        rightFace.setThreeStatus(tempOne);
        rightFace.setSixStatus(tempTwo);
        rightFace.setNineStatus(tempThree);
    }

    // 单独一个面左转，比如前面，3号位的值进入1号位，6号位进入2号位
    // 9号位进入3号位，8号位进入6号位，以此类推，方便后面的转动操作复用这个函数
    private void faceToLeft(Face face) {
        int tempOne = face.getOneStatus(); // 设置两个中转值temp
        int tempTwo = face.getTwoStatus();
        face.setOneStatus(face.getThreeStatus()); // 3进1
        face.setTwoStatus(face.getSixStatus()); // 6进2
        face.setThreeStatus(face.getNineStatus()); // 9进3
        face.setSixStatus(face.getEightStatus()); // 8进6
        face.setNineStatus(face.getSevenStatus());
        face.setEightStatus(face.getFourStatus());
        face.setSevenStatus(tempOne);
        face.setFourStatus(tempTwo);
    }

    // 单独一个面右转
    private void faceToRight(Face face) {
        int tempTwo = face.getTwoStatus();
        int tempOne = face.getOneStatus();
        face.setTwoStatus(face.getFourStatus());
        face.setOneStatus(face.getSevenStatus());
        face.setFourStatus(face.getEightStatus());
        face.setSevenStatus(face.getNineStatus());
        face.setEightStatus(face.getSixStatus());
        face.setNineStatus(face.getThreeStatus());
        face.setSixStatus(tempTwo);
        face.setThreeStatus(tempOne);
    }

    // 判断魔方是否完全复原成功
    public boolean isRubikFinished() {
        return isFaceFinished(upFace) && isFaceFinished(frontFace) && isFaceFinished(rightFace)
                && isFaceFinished(backFace) && isFaceFinished(leftFace) && isFaceFinished(downFace);
    }

    private boolean isFaceFinished(Face face) { // 判断某个面是否完成
        return face.getOneStatus() == face.getFiveStatus() && face.getTwoStatus() == face.getFiveStatus()
                && face.getThreeStatus() == face.getFiveStatus() && face.getFourStatus() == face.getFiveStatus()
                && face.getSixStatus() == face.getFiveStatus() && face.getSevenStatus() == face.getFiveStatus()
                && face.getEightStatus() == face.getFiveStatus() && face.getNineStatus() == face.getFiveStatus();
    }

}
