package com.nexusfc.api.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.nexusfc.api.Model.Simulation;

@Repository
public interface SimulationsRepository extends MongoRepository<Simulation, String> {
    
    @Query("{ '$or': [ { 'desafiante': ?0 }, { 'desafiado_id': ?0 } ] }")
    Page<Simulation> findSimulationHistoryByUserId(Pageable pageable, ObjectId userId);
}
