package com.kafka.communication.Objects;

import lombok.AllArgsConstructor;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Output {
	

	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String OrderID;
	private String ProductID;
	private int Quantity;
	private String Action;
	
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC")
	private Date TimeStamp;
	private String Status;
	private String Comments;
	private String KafkaStatus;

}
