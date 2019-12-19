package be.cite.sample.web.rest;

import be.cite.sample.domain.Message;
import be.cite.sample.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api")
public class MessageRessource {

    private final MessageService messageService;



    static AtomicInteger counter = new AtomicInteger(0);

    public MessageRessource(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/generate-messages")
    ResponseEntity<List<Message>> generateMessages(@RequestParam Integer size){
        final String name = Thread.currentThread().getName();
        int start = counter.getAndAdd(size);
        int end = start + size;
        List<Message> messages = messageService.generateMessages(name, start, end);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }


}
