package ;

import lombok.Data;




/**
 * @author Administrator
 * @version 2020-08-15 18:01:48
 */
@Data
public class PfwTListener {
	/**  */
	private String ID;
	/** 帐号 */
	private String loginName;
	/** 登录次数 */
	private String loginCount;
	/** 状态 */
	private String status;
	/** 最近操作 */
	private String done;
	/** 登录IP */
	private String ip;
	/** 最近登录时间 */
	private String lastLoginTime;
	/** web session */
	private String sessionId;

}
