package es.um.asio.importer.cvn.mapper;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.api.Randomizer;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

import es.um.asio.importer.cvn.mapper.CvnRootBeanMapper;
import es.um.asio.importer.cvn.model.beans.CvnAuthorBean;
import es.um.asio.importer.cvn.model.beans.CvnDateDayMonthYear;
import es.um.asio.importer.cvn.model.beans.CvnItemBean;
import es.um.asio.importer.cvn.model.beans.CvnRootBean;

public class CvnRootBeanMapperTest {
    
    private final CvnRootBeanMapper mapper = Mappers.getMapper(CvnRootBeanMapper.class);
    
    @Test
    public void whenMapCvnRootBean_thenMapToNotNullMappedEntity() {        
        CvnRootBean cvn = new CvnRootBean();
        
        var cvnMapped = mapper.map(cvn);
        
        assertNotNull(cvnMapped);
    }
    
    @Test
    public void whenMapAComplexCvnRootBean_thenMapToNotNullMappedEntity() {        
        CvnRootBean complexCvn = givenAComplexCvnRootBean();
        
        var cvnMapped = mapper.map(complexCvn);
        
        assertNotNull(cvnMapped);
    }
    
    @Test
    public void whenMapCvnRootBean_thenMapSameNumberOfItemBeans() {        
        CvnRootBean cvn = new CvnRootBean();
        cvn.getCvnItemBean().add(new CvnItemBean());
        cvn.getCvnItemBean().add(new CvnItemBean());

        var cvnMapped = mapper.map(cvn);
        
        assertThat(cvnMapped.getCvnItemBean().size()).isEqualTo(2);
    }
     
    @Test
    public void whenMapCvnRootBean_thenMapAuthor() {        
        CvnRootBean cvn = new CvnRootBean();
        CvnItemBean cvnItemBean = new CvnItemBean();
        cvnItemBean.getCvnAuthorBean().add(new CvnAuthorBean());
        cvn.getCvnItemBean().add(cvnItemBean);
 
        var cvnMapped = mapper.map(cvn);
        var authorMapped = cvnMapped.getCvnItemBean().get(0).getCvnAuthorBean();
        
        assertNotNull(authorMapped);
    }
    
    @Test
    public void whenMapCvnRootBean_thenMapGivenNameOfAuthor() {        
        CvnRootBean cvn = new CvnRootBean();
        CvnItemBean cvnItemBean = new CvnItemBean();
        CvnAuthorBean author = new CvnAuthorBean();
        author.setGivenName("Dummy name");
        cvnItemBean.getCvnAuthorBean().add(author);
        cvn.getCvnItemBean().add(cvnItemBean);
 
        var cvnMapped = mapper.map(cvn);
        var authorGivenNameMapped = cvnMapped.getCvnItemBean().get(0).getCvnAuthorBean().get(0).getGivenName();
        
        assertThat(authorGivenNameMapped).isEqualTo("Dummy name");
    }
    
    @Test
    public void whenMapCvnRootBean_thenConvertXMLGregorianCalendarToDate() throws DatatypeConfigurationException {
        Date expectedDate = new Date(2000, 06, 30);
        CvnRootBean cvn = new CvnRootBean();
        CvnItemBean cvnItemBean = new CvnItemBean();
        CvnDateDayMonthYear dateDayMonthYear = new CvnDateDayMonthYear();
        dateDayMonthYear.setValue(givenAXMLGregorianCalendar(expectedDate));
        cvnItemBean.getCvnDateDayMonthYear().add(dateDayMonthYear);
        cvn.getCvnItemBean().add(cvnItemBean);
 
        var cvnMapped = mapper.map(cvn);
        var dateDayMonthYearMapped = cvnMapped.getCvnItemBean().get(0).getCvnDateDayMonthYear().get(0).getValue();
        
        assertThat(dateDayMonthYearMapped).isEqualTo(expectedDate);
    }
    
    private XMLGregorianCalendar givenAXMLGregorianCalendar(Date date) throws DatatypeConfigurationException {
        GregorianCalendar calendar = new GregorianCalendar();       
        calendar.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
    }
    
    private CvnRootBean givenAComplexCvnRootBean() {
        EasyRandomParameters parameters = new EasyRandomParameters().randomize(XMLGregorianCalendar.class, new Randomizer<XMLGregorianCalendar>() {
            @Override
            public XMLGregorianCalendar getRandomValue() {
                GregorianCalendar calendar = new GregorianCalendar();       
                calendar.setTime(Calendar.getInstance().getTime());
                try {
                    return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
                } catch (DatatypeConfigurationException e) {
                    return null;
                }
            }})
            .randomizationDepth(3)
            .objectPoolSize(1000);//to not reuse instances
        EasyRandom generator = new EasyRandom(parameters);
        CvnRootBean cvnRootBean = generator.nextObject(CvnRootBean.class);
        
        return cvnRootBean;
    }
}
