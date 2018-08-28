package com.lhm.activemq;

import java.util.Random;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ActiveMqTest implements Runnable{
	
		private String producerUrl = "failover:(tcp://127.0.0.1:61617,tcp://127.0.0.1:61618)?randomize=false"; 
		private String ConsumerUrl = "failover:(tcp://127.0.0.1:61616,tcp://127.0.0.1:61617,tcp://127.0.0.1:61618)?randomize=false";
		
		
	   public void testMQProducerQueue() throws Exception{
	        //1、创建工厂连接对象，需要制定ip和端口号
	        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(producerUrl);
	        //2、使用连接工厂创建一个连接对象
	        Connection connection = connectionFactory.createConnection();
	        //3、开启连接
	        connection.start();
	        //4、使用连接对象创建会话（session）对象
	        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	        //5、使用会话对象创建目标对象，包含queue和topic（一对一和一对多）
	        Queue queue = session.createQueue("test-queue");
	        //6、使用会话对象创建生产者对象
	        MessageProducer producer = session.createProducer(queue);
	        //7、使用会话对象创建一个消息对象
	        for(int i=0;i<100;i++) {
		        TextMessage textMessage = session.createTextMessage("消息" + i);
		        //8、发送消息
		        producer.send(textMessage);
	        }
	        //9、关闭资源
	        producer.close();
	        session.close();
	        connection.close();
	    }
	   
	    public void TestMQConsumerQueue(final String id) throws Exception{
	        //1、创建工厂连接对象，需要制定ip和端口号
	        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ConsumerUrl);
	        //2、使用连接工厂创建一个连接对象
	        Connection connection = connectionFactory.createConnection();
	        //3、开启连接
	        connection.start();
	        //4、使用连接对象创建会话（session）对象
	        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	        //5、使用会话对象创建目标对象，包含queue和topic（一对一和一对多）
	        Queue queue = session.createQueue("test-queue");
	        //6、使用会话对象创建生产者对象
	        MessageConsumer consumer = session.createConsumer(queue);
	        //7、向consumer对象中设置一个messageListener对象，用来接收消息
	        consumer.setMessageListener(new MessageListener() {

				public void onMessage(Message message) {
	                // TODO Auto-generated method stub
	                if(message instanceof TextMessage){
	                    TextMessage textMessage = (TextMessage)message;
	                    try {
	                        System.out.println("消费者" + id + ":" + textMessage.getText());
	                    } catch (JMSException e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
	                    }
	                }
				}

	        });
	        //8、程序等待接收用户消息
	        System.in.read();
	        //9、关闭资源
	        consumer.close();
	        session.close();
	        connection.close();
	    }
	    
	    //topic生产者测试
	    public void TestTopicProducer() throws Exception{
	        //1、创建工厂连接对象，需要制定ip和端口号
	        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(producerUrl);
	        //2、使用连接工厂创建一个连接对象
	        Connection connection = connectionFactory.createConnection();
	        //3、开启连接
	        connection.start();
	        //4、使用连接对象创建会话（session）对象
	        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	        //5、使用会话对象创建目标对象，包含queue和topic（一对一和一对多）
	        Topic topic = session.createTopic("test-topic");
	        //6、使用会话对象创建生产者对象
	        MessageProducer producer = session.createProducer(topic);
	        //7、使用会话对象创建一个消息对象
	        TextMessage textMessage = session.createTextMessage("hello!test-topic");
	        //8、发送消息
	        producer.send(textMessage);
	        //9、关闭资源
	        producer.close();
	        session.close();
	        connection.close();
	    }
	    
	    public void TestTopicConsumer() throws Exception{
	        //1、创建工厂连接对象，需要制定ip和端口号
	        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ConsumerUrl);
	        //2、使用连接工厂创建一个连接对象
	        Connection connection = connectionFactory.createConnection();
	        //3、开启连接
	        connection.start();
	        //4、使用连接对象创建会话（session）对象
	        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	        //5、使用会话对象创建目标对象，包含queue和topic（一对一和一对多）
	        Topic topic = session.createTopic("test-topic");
	        //6、使用会话对象创建生产者对象
	        MessageConsumer consumer = session.createConsumer(topic);
	        //7、向consumer对象中设置一个messageListener对象，用来接收消息
	        consumer.setMessageListener(new MessageListener() {

				public void onMessage(Message message) {
	                // TODO Auto-generated method stub
	                if(message instanceof TextMessage){
	                    TextMessage textMessage = (TextMessage)message;
	                    try {
	                        System.out.println(textMessage.getText());
	                    } catch (JMSException e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
	                    }
	                }
					
				}

	        });
	        //8、程序等待接收用户消息
	        System.in.read();
	        //9、关闭资源
	        consumer.close();
	        session.close();
	        connection.close();
	    }
	    
	    public static void main(String[] args) throws Exception {
	    	ActiveMqTest test = new ActiveMqTest();
	    	//队列测试
//	    	test.testMQProducerQueue();
	    	test.TestMQConsumerQueue("1");
//	    	test.TestMQConsumerQueue("2");
	    	
	    	//主题测试
//	    	test.TestTopicConsumer();
//	    	test.TestTopicProducer();	
//	    	ActiveMqTest test1 = new ActiveMqTest();
//	    	 new Thread(test1).start();
//	    	 
//	    	ActiveMqTest test2 = new ActiveMqTest();
//	    	 new Thread(test2).start();
		}

		public void run() {
			try {
				Random rd = new Random();
				
				TestMQConsumerQueue(String.valueOf(rd.nextInt()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    
}

