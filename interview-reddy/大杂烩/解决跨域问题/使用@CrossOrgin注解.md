如果只是想部分接口跨域，且不想使用配置来管理的话，可以使用这种方式
* 在Controller使用
```java
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

	@GetMapping("/{id}")
	public User get(@PathVariable Long id) {
		
	}

	@DeleteMapping("/{id}")
	public void remove(@PathVariable Long id) {

	}
}
```
* 在具体接口上使用
```java
@RestController
@RequestMapping("/user")
public class UserController {

	@CrossOrigin
	@GetMapping("/{id}")
	public User get(@PathVariable Long id) {
		
	}

	@DeleteMapping("/{id}")
	public void remove(@PathVariable Long id) {

	}
}
```