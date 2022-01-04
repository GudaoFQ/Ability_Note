import io.xjar.boot.XBoot;

/**
 * @Description TODO
 * @Author zhangxiaoyu
 * @Date 2020/4/30 14:04
 */
public class XjarDemo {

	public static void main(String[] args) throws Exception {
		XBoot.encrypt(
				"D:\\Project\\Code\\pcab\\cda-admin\\target\\sca.jar",
				"D:\\xjar\\linux\\sca-v2.0.jar",
				"test@gudao",
				(entry) -> {
					String name = entry.getName();
					String pkg = "com/cda/web";
					return name.startsWith(pkg);
				}
		);
	}
}



