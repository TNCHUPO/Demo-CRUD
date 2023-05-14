package com.obakeng.cruddemo;

import static org.assertj.core.api.Assertions.assertThat;

import com.obakeng.cruddemo.model.BmwCrudDemo;
import com.obakeng.cruddemo.repository.BmwCrudRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@DataJpaTest
public class JPAUnitTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  BmwCrudRepository repository;

  @Test
  public void should_find_no_tutorials_if_repository_is_empty() {
    Iterable<BmwCrudDemo> tutorials = repository.findAll();

    assertThat(tutorials).isEmpty();
  }

  @Test
  public void should_store_a_car() {
    BmwCrudDemo cars = repository.save(new BmwCrudDemo("Tut car", "Tut desc", true));

    assertThat(cars).hasFieldOrPropertyWithValue("car", "Tut car");
    assertThat(cars).hasFieldOrPropertyWithValue("description", "Tut desc");
    assertThat(cars).hasFieldOrPropertyWithValue("settled", true);
  }

  @Test
  public void should_find_all_car() {
    BmwCrudDemo tut1 = new BmwCrudDemo("Tut#1", "Desc#1", true);
    entityManager.persist(tut1);

    BmwCrudDemo tut2 = new BmwCrudDemo("Tut#2", "Desc#2", false);
    entityManager.persist(tut2);

    BmwCrudDemo tut3 = new BmwCrudDemo("Tut#3", "Desc#3", true);
    entityManager.persist(tut3);

    Iterable<BmwCrudDemo> tutorials = repository.findAll();

    assertThat(tutorials).hasSize(3).contains(tut1, tut2, tut3);
  }

  @Test
  public void should_find_car_by_id() {
    BmwCrudDemo tut1 = new BmwCrudDemo("Tut#1", "Desc#1", true);
    entityManager.persist(tut1);

    BmwCrudDemo tut2 = new BmwCrudDemo("Tut#2", "Desc#2", false);
    entityManager.persist(tut2);

    BmwCrudDemo foundCar = repository.findById(tut2.getId()).get();

    assertThat(foundCar).isEqualTo(tut2);
  }



  @Test
  public void should_update_car_by_id() {
    BmwCrudDemo tut1 = new BmwCrudDemo("Tut#1", "Desc#1", true);
    entityManager.persist(tut1);

    BmwCrudDemo tut2 = new BmwCrudDemo("Tut#2", "Desc#2", false);
    entityManager.persist(tut2);

    BmwCrudDemo updatedTut = new BmwCrudDemo("updated Tut#2", "updated Desc#2", true);

    BmwCrudDemo tut = repository.findById(tut2.getId()).get();
    tut.setCar(updatedTut.getCar());
    tut.setDescription(updatedTut.getDescription());
    tut.setSettled(updatedTut.isSettled());
    repository.save(tut);

    BmwCrudDemo checkTut = repository.findById(tut2.getId()).get();
    
    assertThat(checkTut.getId()).isEqualTo(tut2.getId());
    assertThat(checkTut.getCar()).isEqualTo(updatedTut.getCar());
    assertThat(checkTut.getDescription()).isEqualTo(updatedTut.getDescription());
    assertThat(checkTut.isSettled()).isEqualTo(updatedTut.isSettled());
  }

  @Test
  public void should_delete_car_by_id() {
    BmwCrudDemo tut1 = new BmwCrudDemo("Tut#1", "Desc#1", true);
    entityManager.persist(tut1);

    BmwCrudDemo tut2 = new BmwCrudDemo("Tut#2", "Desc#2", false);
    entityManager.persist(tut2);

    BmwCrudDemo tut3 = new BmwCrudDemo("Tut#3", "Desc#3", true);
    entityManager.persist(tut3);

    repository.deleteById(tut2.getId());

    Iterable<BmwCrudDemo> tutorials = repository.findAll();

    assertThat(tutorials).hasSize(2).contains(tut1, tut3);
  }

  @Test
  public void should_delete_all_car() {
    entityManager.persist(new BmwCrudDemo("Tut#1", "Desc#1", true));
    entityManager.persist(new BmwCrudDemo("Tut#2", "Desc#2", false));

    repository.deleteAll();

    assertThat(repository.findAll()).isEmpty();
  }
}
