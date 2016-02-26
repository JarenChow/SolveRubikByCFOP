/**
 * Created by Administrator on 2015/11/14 0014.
 */
public class Face {

    private int oneStatus;

    private int twoStatus;

    private int threeStatus;

    private int fourStatus;

    private int fiveStatus;

    private int sixStatus;

    private int sevenStatus;

    private int eightStatus;

    private int nineStatus;

    public Face() {

    }

    public void init(int status) {
        this.oneStatus = status;
        this.twoStatus = status;
        this.threeStatus = status;
        this.fourStatus = status;
        this.fiveStatus = status;
        this.sixStatus = status;
        this.sevenStatus = status;
        this.eightStatus = status;
        this.nineStatus = status;
    }

    public void init(int oneStatus, int twoStatus, int threeStatus, int fourStatus, int fiveStatus,
                     int sixStatus, int sevenStatus, int eightStatus, int nineStatus) {
        this.oneStatus = oneStatus;
        this.twoStatus = twoStatus;
        this.threeStatus = threeStatus;
        this.fourStatus = fourStatus;
        this.fiveStatus = fiveStatus;
        this.sixStatus = sixStatus;
        this.sevenStatus = sevenStatus;
        this.eightStatus = eightStatus;
        this.nineStatus = nineStatus;
    }

    public int getOneStatus() {
        return oneStatus;
    }

    public void setOneStatus(int oneStatus) {
        this.oneStatus = oneStatus;
    }

    public int getTwoStatus() {
        return twoStatus;
    }

    public void setTwoStatus(int twoStatus) {
        this.twoStatus = twoStatus;
    }

    public int getThreeStatus() {
        return threeStatus;
    }

    public void setThreeStatus(int threeStatus) {
        this.threeStatus = threeStatus;
    }

    public int getFourStatus() {
        return fourStatus;
    }

    public void setFourStatus(int fourStatus) {
        this.fourStatus = fourStatus;
    }

    public int getFiveStatus() {
        return fiveStatus;
    }

    public void setFiveStatus(int fiveStatus) {
        this.fiveStatus = fiveStatus;
    }

    public int getSixStatus() {
        return sixStatus;
    }

    public void setSixStatus(int sixStatus) {
        this.sixStatus = sixStatus;
    }

    public int getSevenStatus() {
        return sevenStatus;
    }

    public void setSevenStatus(int sevenStatus) {
        this.sevenStatus = sevenStatus;
    }

    public int getEightStatus() {
        return eightStatus;
    }

    public void setEightStatus(int eightStatus) {
        this.eightStatus = eightStatus;
    }

    public int getNineStatus() {
        return nineStatus;
    }

    public void setNineStatus(int nineStatus) {
        this.nineStatus = nineStatus;
    }

}
