package es.um.asio.importer.marshaller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import es.um.asio.abstractions.domain.Operation;
import es.um.asio.importer.helper.DummyOperationableDataSetData;

public class DataSetFieldSetMapperTest {
    
    private DataSetFieldSetMapper<DummyOperationableDataSetData> mapper;
    
    @Before
    public void setUp() {
        mapper = new DataSetFieldSetMapper<DummyOperationableDataSetData>(DummyOperationableDataSetData.class);
    }

    @Test
    public void whenMapOperationWithADDStringValue_thenConvertsToInsertOperation() throws BindException {
        FieldSet fieldSet = new DefaultFieldSet(new String[] { "ADD"}, new String[] { "operation"});
       
        DummyOperationableDataSetData result =  mapper.mapFieldSet(fieldSet);
    
        assertThat(result.getOperation()).isEqualTo(Operation.INSERT);
    }
    
    @Test
    public void whenMapOperationWithIncorrectStringValue_thenConvertsToInsertOperation() throws BindException {
        FieldSet fieldSet = new DefaultFieldSet(new String[] { "incorrect value"}, new String[] { "operation"});
       
        DummyOperationableDataSetData result =  mapper.mapFieldSet(fieldSet);
    
        assertThat(result.getOperation()).isEqualTo(Operation.INSERT);
    }
    
    @Test
    public void whenMapOperationWithEmptyStringValue_thenConvertsToInsertOperation() throws BindException {
        FieldSet fieldSet = new DefaultFieldSet(new String[] { ""}, new String[] { "operation"});
       
        DummyOperationableDataSetData result =  mapper.mapFieldSet(fieldSet);
    
        assertThat(result.getOperation()).isEqualTo(Operation.INSERT);
    }
    
    @Test
    public void whenMapOperationWithNullValue_thenConvertsToInsertOperation() throws BindException {
        FieldSet fieldSet = new DefaultFieldSet(new String[] { null }, new String[] { "operation"});
       
        DummyOperationableDataSetData result =  mapper.mapFieldSet(fieldSet);
    
        assertThat(result.getOperation()).isEqualTo(Operation.INSERT);
    }
    
    @Test
    public void whenMapWithoutOperationField_thenConvertsToInsertOperation() throws BindException {
        FieldSet fieldSet = new DefaultFieldSet(new String[] { "dummy value" }, new String[] { "dummyProperty"});
       
        DummyOperationableDataSetData result =  mapper.mapFieldSet(fieldSet);
    
        assertThat(result.getOperation()).isEqualTo(Operation.INSERT);
    }
    
    @Test
    public void whenMapOperationWithMODIFYStringValue_thenConvertsToUpdateOperation() throws BindException {
        FieldSet fieldSet = new DefaultFieldSet(new String[] { "MODIFY"}, new String[] { "operation"});
       
        DummyOperationableDataSetData result =  mapper.mapFieldSet(fieldSet);
    
        assertThat(result.getOperation()).isEqualTo(Operation.UPDATE);
    }
    
    @Test
    public void whenMapOperationWithDELETEStringValue_thenConvertsToDeleteOperation() throws BindException {
        FieldSet fieldSet = new DefaultFieldSet(new String[] { "DELETE"}, new String[] { "operation"});
       
        DummyOperationableDataSetData result =  mapper.mapFieldSet(fieldSet);
    
        assertThat(result.getOperation()).isEqualTo(Operation.DELETE);
    }
}
