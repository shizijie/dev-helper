package ;

import lombok.Data;




/**
 * @author Administrator
 * @version 2020-08-15 18:32:18
 */
@Data
public class PfwTDict {
	/**  */
	private String ID;
	/** 代码 */
	private String dm;
	/** 名称 */
	private String name;
	/** 是否有效 */
	private String sfyx;
	/** 备注 */
	private String remark;
	/** 类型 */
	private String type;
	/**  */
	private String parent;

}
