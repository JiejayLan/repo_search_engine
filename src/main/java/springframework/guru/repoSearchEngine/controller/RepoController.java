package springframework.guru.webclientdemo.controller;
import org.springframework.http.HttpStatus;
import springframework.guru.webclientdemo.dto.Repo;
import springframework.guru.webclientdemo.service.RepoClientEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1")
public class RepoController {
    private static final Logger logger = LoggerFactory.getLogger(RepoController.class);
    private RepoClientEventService repoClientEventService;

    @Autowired
    public RepoController(RepoClientEventService repoClientEventService){
        this.repoClientEventService = repoClientEventService;
    }

    @GetMapping("/search")
    public ResponseEntity<ArrayList<Repo>> getGithubRepo(@RequestParam String q) {
        ArrayList<Repo> results = repoClientEventService.getRepo(q);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
        logger.error("Error from WebClient - Status {}, Body {}", ex.getRawStatusCode(),
                ex.getResponseBodyAsString(), ex);
        return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
    }
}