package bookmarket;

import bookmarket.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @Autowired
    DeliveryRepository deliveryRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaid_Ship(@Payload Paid paid){

        if(paid.isMe()){
            System.out.println("##### listener Ship : " + paid.toJson());
            Delivery delivery = new Delivery();
            delivery.setOrderId(paid.getOrderId());
            delivery.setCustomerId(paid.getCustomerId());
            delivery.setStatus("Shipped");

            deliveryRepository.save(delivery);
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPayCanceled_DeliveryCancel(@Payload PayCanceled payCanceled){

        if(payCanceled.isMe()){
            System.out.println("##### listener DeliveryCancel : " + payCanceled.toJson());
            Delivery delivery = new Delivery();
            delivery.setOrderId(payCanceled.getOrderId());
            delivery.setCustomerId(payCanceled.getCustomerId());
            delivery.setStatus("DeliveryCancelled");

            deliveryRepository.save(delivery);
        }
    }

}
