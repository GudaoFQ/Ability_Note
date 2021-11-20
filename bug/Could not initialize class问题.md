## Exception in thread "AWT-EventQueue-0" java.lang.NoClassDefFoundError: Could not initialize class org.gudao.ResourceMgr

#### 问题描述
> 项目无法启动，并抛出如下异常

```text
Exception in thread "AWT-EventQueue-0" java.lang.NoClassDefFoundError: Could not initialize class org.gudao.ResourceMgr
	at org.gudao.Tank.paint(Tank.java:38)
	at org.gudao.TankFrame.paint(TankFrame.java:78)
	at org.gudao.TankFrame.update(TankFrame.java:61)
	at java.desktop/sun.awt.RepaintArea.updateComponent(RepaintArea.java:255)
	at java.desktop/sun.awt.RepaintArea.paint(RepaintArea.java:232)
	at java.desktop/sun.awt.windows.WComponentPeer.handleEvent(WComponentPeer.java:358)
	at java.desktop/java.awt.Component.dispatchEventImpl(Component.java:5072)
	at java.desktop/java.awt.Container.dispatchEventImpl(Container.java:2321)
	at java.desktop/java.awt.Window.dispatchEventImpl(Window.java:2772)
	at java.desktop/java.awt.Component.dispatchEvent(Component.java:4843)
	at java.desktop/java.awt.EventQueue.dispatchEventImpl(EventQueue.java:772)
	at java.desktop/java.awt.EventQueue$4.run(EventQueue.java:721)
	at java.desktop/java.awt.EventQueue$4.run(EventQueue.java:715)
	at java.base/java.security.AccessController.doPrivileged(Native Method)
	at java.base/java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:85)
	at java.base/java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:95)
	at java.desktop/java.awt.EventQueue$5.run(EventQueue.java:745)
	at java.desktop/java.awt.EventQueue$5.run(EventQueue.java:743)
	at java.base/java.security.AccessController.doPrivileged(Native Method)
	at java.base/java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:85)
	at java.desktop/java.awt.EventQueue.dispatchEvent(EventQueue.java:742)
	at java.desktop/java.awt.EventDispatchThread.pumpOneEventForFilters(EventDispatchThread.java:203)
	at java.desktop/java.awt.EventDispatchThread.pumpEventsForFilter(EventDispatchThread.java:124)
	at java.desktop/java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:113)
	at java.desktop/java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:109)
	at java.desktop/java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:101)
	at java.desktop/java.awt.EventDispatchThread.run(EventDispatchThread.java:90)
```

#### 错误原因
> idea与eclipse不同，静态资源文件需要放在resource目录下，修改目录结构为

在这里插入图片描述

![could not initalize class](../resource/bug/bug-could not initalize class.jpg)