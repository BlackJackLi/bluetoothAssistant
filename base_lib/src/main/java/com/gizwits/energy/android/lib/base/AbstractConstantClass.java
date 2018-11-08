package com.gizwits.energy.android.lib.base;

/**
 * Created by Black on 2016/2/21 0021.
 * 定义一个抽象的用于防止常量类被实例化的基类
 * <br>
 * example:
 * <br>
 * public final class KEY extends AbstractConstantClass {
 * <br>
 * private KEY() {
 * <br>
 * super();
 * <br>
 * }
 * <br>
 * .....
 * <br>
 * }
 * <br>
 * 定义成final类型,定义使用私有构造方法,调用super();
 */
public abstract class AbstractConstantClass {
	protected AbstractConstantClass() {
		throw new Error("don't instantiation this constant class: " + this.getClass().getSimpleName());
	}
}
