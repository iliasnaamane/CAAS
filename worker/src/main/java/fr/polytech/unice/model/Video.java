package fr.polytech.unice.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Video {



    @Id public Long id;
    @Index public String videoName;
    public double videoDuration;
    
    public Video(String videoName, double videoDuration ) {
        this.videoName = videoName;
        this.videoDuration = videoDuration;
       
    }

}
