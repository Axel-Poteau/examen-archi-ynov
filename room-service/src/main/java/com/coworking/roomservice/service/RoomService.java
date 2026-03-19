package com.coworking.roomservice.service;

import com.coworking.roomservice.model.Room;
import com.coworking.roomservice.repository.RoomRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public RoomService(RoomRepository roomRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.roomRepository = roomRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    public List<Room> findByCity(String city) {
        return roomRepository.findByCity(city);
    }

    public List<Room> findAvailable() {
        return roomRepository.findByAvailable(true);
    }

    public Room create(Room room) {
        room.setAvailable(true);
        return roomRepository.save(room);
    }

    public Room update(Long id, Room roomDetails) {
        Room room = findById(id);
        room.setName(roomDetails.getName());
        room.setCity(roomDetails.getCity());
        room.setCapacity(roomDetails.getCapacity());
        room.setType(roomDetails.getType());
        room.setHourlyRate(roomDetails.getHourlyRate());
        return roomRepository.save(room);
    }

    public void delete(Long id) {
        roomRepository.deleteById(id);
        kafkaTemplate.send("room-deleted", id.toString());
    }

    public void setAvailability(Long id, boolean available) {
        Room room = findById(id);
        room.setAvailable(available);
        roomRepository.save(room);
    }
}
