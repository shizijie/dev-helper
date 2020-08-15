package ;

import lombok.Data;




/**
 * @author Administrator
 * @version 2020-08-15 18:06:06
 */
@Data
public class PfwTFilemodel {
	/**  */
	private String ID;
	/** 路径 */
	private String path;
	/** 创建时间 */
	private String createDate;
	/** 创建者 */
	private String creator;
	/** 文件名 */
	private String title;
	/** 文件类型 */
	private String type;
	/** 备注 */
	private String remark;

}
