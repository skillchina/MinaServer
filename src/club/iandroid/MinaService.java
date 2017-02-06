package club.iandroid;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * 一个简单的mina服务器
 * @author Administrator
 *
 */
public class MinaService {

	public static void main(String[] args) {
		IoAcceptor acceptor = new NioSocketAcceptor();
		//添加日志过滤器
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast("codec", 
				new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		acceptor.setHandler(new DemoServerHandler());
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		//监听客户端的连接
		
		try {
			acceptor.bind(new InetSocketAddress(9133));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("启动服务");  
	}

	/**
	 * 负责我们session对象的创建监听以及消息发送和接收的监听
	 * @author Administrator
	 *
	 */
	private static class DemoServerHandler extends IoHandlerAdapter{
		
		@Override
		public void sessionCreated(IoSession session) throws Exception {
			 System.out.println("服务器与客户端创建连接...");  
			super.sessionCreated(session);
		}
		
		@Override
		public void sessionOpened(IoSession session) throws Exception {
			System.out.println("服务器与客户端连接打开...");  
			super.sessionOpened(session);
		}
		
		/**
		 * 消息的接收处理
		 */
		@Override
		public void messageReceived(IoSession session, Object message) throws Exception {
			// TODO Auto-generated method stub
			super.messageReceived(session, message);
			String str = message.toString();
			Date date = new Date();
			session.write(date.toString());
			System.out.println("接收到的数据："+str);
		}
		
		@Override
		public void messageSent(IoSession session, Object message) throws Exception {
			// TODO Auto-generated method stub
			super.messageSent(session, message);
		}
		
		@Override
		public void sessionClosed(IoSession session) throws Exception {
			 System.out.println("服务器与客户端连接关闭...");  
			super.sessionClosed(session);
		}
	}
}
