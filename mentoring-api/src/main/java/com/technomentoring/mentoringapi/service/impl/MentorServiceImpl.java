package com.technomentoring.mentoringapi.service.impl;

import com.technomentoring.mentoringapi.exception.DataAlreadyExistsException;
import com.technomentoring.mentoringapi.exception.ModelNotFoundException;
import com.technomentoring.mentoringapi.model.Mentor;
import com.technomentoring.mentoringapi.repository.IGenericRepository;
import com.technomentoring.mentoringapi.repository.IMentorRepository;
import com.technomentoring.mentoringapi.service.IMentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl extends CRUDImpl<Mentor,Integer> implements IMentorService {
    private final IMentorRepository repo;

    @Override
    protected IGenericRepository<Mentor, Integer> getRepo() {
        return repo;
    }

    @Override
    public Mentor save(Mentor mentor) throws Exception {
        String name = mentor.getName();
        String DNI = mentor.getDNI();
        String email = mentor.getEmail();
        String password = mentor.getPassword();
        if (isMentorDuplicate(name,DNI,email,password)) {
            throw new DataAlreadyExistsException("El mentor con los detalles que ingresaste ya existe.");
        }
        return super.save(mentor);
    }

    @Override
    public Mentor update(Mentor mentor, Integer idMentor) throws Exception {
        String name = mentor.getName();
        String DNI = mentor.getDNI();
        String email = mentor.getEmail();
        String password = mentor.getPassword();

        getRepo().findById(idMentor).orElseThrow(() -> new ModelNotFoundException("EL mentor con id "+ idMentor + " ya existe."));

        if (isMentorDuplicateUpdate(name,DNI,email,password)){
            throw new DataAlreadyExistsException("El mentor con los detalles que ingresaste ya existe.");
        }
        return super.update(mentor, idMentor);
    }

    public boolean isMentorDuplicate(String name, String DNI,String email,String password) {
        return repo.existsByNameOrDNI(name,DNI);
    }

    public boolean isMentorDuplicateUpdate(String name, String DNI,String email,String password) {
        return repo.existsByNameAndDNI(name,DNI);
    }

}
