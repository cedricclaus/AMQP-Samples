package be.cite.sample.web.rest;

import be.cite.sample.domain.Message;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api")
public class MessageRessource {

    static AtomicInteger counter = new AtomicInteger(0);

    @GetMapping("/generate-messages")
    ResponseEntity<List<Message>> generateMessages(@RequestParam Integer size){
        final String name = Thread.currentThread().getName();
        int start = counter.getAndAdd(size+1);
        List<Message> messages = IntStream.rangeClosed(start, start+size)
            .boxed()
            .map(i -> {
                Message msg = new Message();
                msg.setId(new ObjectId().toString());
                msg.setName(name);
                msg.setIndex(i);
                msg.setDate(LocalDateTime.now());
                msg.setGood( Math.random() >= 0.1); //10% of false
                return msg;
            })
            .collect(Collectors.toList());
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }


}
