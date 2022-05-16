package com.example.service.impl;

import com.example.entity.Example;
import com.example.exception.BadRequestException;
import com.example.helper.ObjectMapper;
import com.example.repository.ExampleRepository;
import com.example.service.model.request.ExampleRequestDTO;
import com.example.service.model.request.PagingRequestDTO;
import com.example.service.model.response.ExampleResponseDTO;
import com.example.service.model.response.PagingResponseDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExampleServiceImplTest {

    @InjectMocks
    private ExampleServiceImpl exampleService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ExampleRepository exampleRepository;

    private static final String ID = "id-001";
    private static final String VALUE = "value-001";
    private static final int PAGE = 0;
    private static final int SIZE = 5;

    private Example example1;
    private Example example2;
    private List<Example> examples;
    private ExampleResponseDTO exampleResponseDTO;

    @Before
    public void setUp() {
        generateExample();
    }

    @Test
    public void test_getById_success() {
        when(exampleRepository.findByIdAndDeletedFalse(ID))
            .thenReturn(example1);
        when(objectMapper.map(any(Example.class), eq(ExampleResponseDTO.class)))
            .thenReturn(exampleResponseDTO);

        ExampleResponseDTO responseDTO = exampleService.getById(ID).block();

        verify(exampleRepository).findByIdAndDeletedFalse(ID);
        verify(objectMapper).map(any(Example.class), eq(ExampleResponseDTO.class));

        assertNotNull(responseDTO);
        assertEquals(example1.getValue(), responseDTO.getValue());
    }

    @Test
    public void test_getById_throw_badRequestException() {
        try {
            exampleService.getById(ID).block();
        } catch (BadRequestException e) {
            verify(exampleRepository).findByIdAndDeletedFalse(ID);
        }
    }

    @Test
    public void test_getAll_success() {
        PagingRequestDTO pagingRequest = PagingRequestDTO.builder()
            .page(PAGE)
            .size(SIZE)
            .build();

        when(exampleRepository.findAllByDeletedFalse(any(PageRequest.class)))
            .thenReturn(new PageImpl<>(examples));
        when(objectMapper.map(any(Example.class), eq(ExampleResponseDTO.class)))
            .thenReturn(exampleResponseDTO);

        PagingResponseDTO<ExampleResponseDTO> responseDTO =
            exampleService.getAll(pagingRequest).block();

        verify(exampleRepository).findAllByDeletedFalse(any(PageRequest.class));
        verify(objectMapper, times(2)).map(any(Example.class), eq(ExampleResponseDTO.class));

        assertNotNull(responseDTO);
        assertEquals(examples.size(), responseDTO.getEntries().size());
    }

    @Test
    public void test_insert_success() {
        ExampleRequestDTO requestDTO = ExampleRequestDTO.builder()
            .value(VALUE)
            .build();

        when(objectMapper.map(any(ExampleRequestDTO.class), eq(Example.class)))
            .thenReturn(example1);
        when(exampleRepository.save(any()))
            .thenReturn(example1);
        when(objectMapper.map(any(Example.class), eq(ExampleResponseDTO.class)))
            .thenReturn(exampleResponseDTO);

        ExampleResponseDTO responseDTO = exampleService.insert(requestDTO).block();

        verify(exampleRepository).save(any(Example.class));
        verify(objectMapper).map(any(Example.class), eq(ExampleResponseDTO.class));

        assertNotNull(responseDTO);
        assertEquals(VALUE, responseDTO.getValue());
    }

    @Test
    public void test_update_success() {
        ExampleRequestDTO requestDTO = ExampleRequestDTO.builder()
            .value(VALUE)
            .build();

        when(exampleRepository.findByIdAndDeletedFalse(ID))
            .thenReturn(example1);
        when(exampleRepository.save(any()))
            .thenReturn(example1);
        when(objectMapper.map(any(Example.class), eq(ExampleResponseDTO.class)))
            .thenReturn(exampleResponseDTO);

        ExampleResponseDTO responseDTO = exampleService.update(ID, requestDTO).block();

        verify(exampleRepository).findByIdAndDeletedFalse(ID);
        verify(exampleRepository).save(any(Example.class));
        verify(objectMapper).map(any(Example.class), eq(ExampleResponseDTO.class));

        assertNotNull(responseDTO);
        assertEquals(VALUE, responseDTO.getValue());
    }

    @Test
    public void test_update_throw_badRequestException() {
        try {
            ExampleRequestDTO requestDTO = ExampleRequestDTO.builder()
                .value(VALUE)
                .build();

            exampleService.update(ID, requestDTO).block();
        } catch (BadRequestException e) {
            verify(exampleRepository).findByIdAndDeletedFalse(ID);
        }
    }

    @Test
    public void test_delete_success() {
        when(exampleRepository.findByIdAndDeletedFalse(ID))
            .thenReturn(example1);
        when(exampleRepository.save(any()))
            .thenReturn(example1);

        Boolean responseDTO = exampleService.delete(ID).block();

        verify(exampleRepository).findByIdAndDeletedFalse(ID);
        verify(exampleRepository).save(any(Example.class));

        assertNotNull(responseDTO);
        assertTrue(responseDTO);
    }

    @Test
    public void test_delete_throw_badRequestException() {
        try {
            exampleService.delete(ID).block();
        } catch (BadRequestException e) {
            verify(exampleRepository).findByIdAndDeletedFalse(ID);
        }
    }

    private void generateExample() {
        example1 = Example.builder()
            .id(ID)
            .value(VALUE)
            .deleted(false)
            .build();

        example2 = Example.builder()
            .id(ID)
            .value(VALUE)
            .deleted(false)
            .build();

        examples = new ArrayList<>();
        examples.add(example1);
        examples.add(example2);

        exampleResponseDTO = ExampleResponseDTO.builder()
            .id(ID)
            .value(VALUE)
            .build();
    }

}
