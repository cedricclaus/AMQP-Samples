package be.cite.sample.domain;

import org.bson.Document;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.beans.BeanUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Date;

public class MongoDBRabbitMessage {

    public MongoDBRabbitMessage(Message msg) throws UnsupportedEncodingException {
        this.headers = new Document(msg.getMessageProperties().getHeaders());
        this.body =   Document.parse(new String(msg.getBody(), Charset.defaultCharset().name()));
        MessageProperties messageProperties = new MessageProperties();
        BeanUtils.copyProperties(msg.getMessageProperties(), messageProperties);
        this.messageProperties =messageProperties;
    }

    Document headers;
    Document body;
    MessageProperties messageProperties;

    public Document getHeaders() {
        return headers;
    }

    public void setHeaders(Document headers) {
        this.headers = headers;
    }

    public Document getBody() {
        return body;
    }

    public void setBody(Document body) {
        this.body = body;
    }

    public MessageProperties getMessageProperties() {
        return messageProperties;
    }

    public void setMessageProperties(MessageProperties messageProperties) {
        this.messageProperties = messageProperties;
    }
}

class MessageProperties{
    private volatile Date timestamp;

    private volatile String messageId;

    private volatile String userId;

    private volatile String appId;

    private volatile String clusterId;

    private volatile String type;

    private volatile String correlationId;

    private volatile String replyTo;

    private volatile String contentType;

    private volatile String contentEncoding;

    private volatile long contentLength;

    private volatile boolean contentLengthSet;

    private volatile MessageDeliveryMode deliveryMode;

    private volatile String expiration;

    private volatile Integer priority;

    private volatile Boolean redelivered;

    private volatile String receivedExchange;

    private volatile String receivedRoutingKey;

    private volatile String receivedUserId;

    private volatile long deliveryTag;

    private volatile boolean deliveryTagSet;

    private volatile Integer messageCount;

    // Not included in hashCode()

    private volatile String consumerTag;

    private volatile String consumerQueue;

    private volatile Integer receivedDelay;

    private volatile String receivedDeliveryMode;

    private volatile boolean finalRetryForMessageWithNoId;

    private volatile long publishSequenceNumber;
}
