package de.pincservices.gtd.repository;

import de.pincservices.gtd.model.Due;
import org.springframework.data.repository.CrudRepository;

public interface DueRepository extends CrudRepository<Due, String> {
}
