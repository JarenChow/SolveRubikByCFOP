# 魔方还原的算法
1. Face类, 代表面, 有六个颜色, 
2. Rubik类, 九个面组成, 使用CFOP四步还原魔方
3. RestoreRubik类是测试类, 直接运行这个类
## 代码写的比较丑, 以后慢慢学的老到了应该能写出更高质量的代码

 * Created by Jia en on 2015/11/14 0014.
 * Rubik    getInstance()       获得Rubik的唯一实例
 * void     init()              初始化
 * Face     get{}Face().init()  初始化面的值,up,front,right,back,left,down
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
 
 ![image](http://github.com/M3oM3oBug/SolveRubikByCFOP/raw/master/rubik.gif)