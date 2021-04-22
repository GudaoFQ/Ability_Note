## WebUpload参数配置说明

### Mediator
> 中介者，它本身是个单例，但可以通过installTo方法，使任何对象具备事件行为。 主要目的是负责模块与模块之间的合作，降低耦合度。

#### on
  * on( name, callback[, context] ) ⇒ self
* 参数:
  * name {String}事件名，支持多个事件用空格隔开
  * callback {Function}事件处理器
  * context {Object} [可选]事件处理器的上下文。
* 返回值:
  * {self}
      > 返回自身，方便链式
* 绑定事件。
  * callback方法在执行时，arguments将会来源于trigger的时候携带的参数。如
  ```js
  var obj = {};
  
  // 使得obj有事件行为
  Mediator.installTo( obj );
  
  obj.on( 'testa', function( arg1, arg2 ) {
      console.log( arg1, arg2 ); // => 'arg1', 'arg2'
  });
  
  obj.trigger( 'testa', 'arg1', 'arg2' );
  ```
  * 如果callback中，某一个方法return false了，则后续的其他callback都不会被执行到。 切会影响到trigger方法的返回值，为false。
  * on还可以用来添加一个特殊事件all, 这样所有的事件触发都会响应到。同时此类callback中的arguments有一个不同处， 就是第一个参数为type，记录当前是什么事件在触发。此类callback的优先级比脚低，会再正常callback执行完后触发。
  ```js
  obj.on( 'all', function( type, arg1, arg2 ) {
    console.log( type, arg1, arg2 ); // => 'testa', 'arg1', 'arg2'
  });
  ```

#### once
  * once( name, callback[, context] ) ⇒ self
* 参数:
  * name {String}事件名
  * callback {Function}事件处理器
  * context {Object} [可选]事件处理器的上下文。
* 返回值:
  {self}
    > 返回自身，方便链式
> 绑定事件，且当handler执行完后，自动解除绑定。

#### off
  * off( [name[, callback[, context] ] ] ) ⇒ self
* 参数:
  * name {String} [可选]事件名
  * callback {Function} [可选]事件处理器
  * context {Object} [可选]事件处理器的上下文。
* 返回值:
  * {self}
      > 返回自身，方便链式
> 解除事件绑定

#### trigger
  * trigger( name[, args...] ) ⇒ self
* 参数:
  * type 
      > {String}事件名 
  * ... {*} [可选]
      > 任意参数
* 返回值:
  * {Boolean}
    > 如果handler中return false了，则返回false, 否则返回true 
> 触发事件

#### installTo
* 参数:
  * obj {Object}
      > 需要具备事件行为的对象。
* 返回值:
  * {Object}
      > 返回obj.
> 可以通过这个接口，使任何对象具备事件功能。