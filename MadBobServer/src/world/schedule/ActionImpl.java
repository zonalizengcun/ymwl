package world.schedule;

/**
 * 行为接口
 * @author yunlong.xu
 *
 */
public interface ActionImpl {

	public Action getAction();


	/**
	 * 行为实现
	 */
	public abstract void exec() throws Exception;

	/**
	 * 行为完成回调方法
	 */
	public abstract void over() throws Exception;

}
