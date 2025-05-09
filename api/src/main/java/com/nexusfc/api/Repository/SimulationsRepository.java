package com.nexusfc.api.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexusfc.api.Model.Simulation;

@Repository
public interface SimulationsRepository extends MongoRepository<Simulation, String> {}
