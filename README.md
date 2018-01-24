# 魔方还原的算法
1. Face类, 代表面, 有六个颜色, 
2. Rubik类, 六个面组成, 使用CFOP四步还原魔方
3. RestoreRubik类是测试类, 直接运行这个类

 * Created by Jia en on 2015/11/14 0014.
 * Rubik    getInstance()       获得Rubik的唯一实例
 * void     init()              初始化
 * Face     get{}Face().init()  初始化面的值,up,front,right,back,left,down
 * String   getDisruptedData()  获得打乱步骤的数据
 * void     handleTurn(String)  打乱魔方
 * void     cross()             运行Cross
 * void     f2l()               运行F2L
 * void     oll()               运行OLL
 * void     pll()               运行PLL
 * String   getCrossData()      获得Cross的数据
 * String   getF2lData()        获得F2L的数据
 * String   getOllData()        获得OLL的数据
 * String   getPllData()        获得PLL的数据
 * boolean  isRubikFinished()   判断魔方是否复原
 
 ![image](https://github.com/M3oM3oBug/SolveRubikByCFOP/raw/master/rubik.gif)
