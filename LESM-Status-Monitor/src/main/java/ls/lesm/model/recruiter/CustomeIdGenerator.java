package ls.lesm.model.recruiter;



import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

public class CustomeIdGenerator extends SequenceStyleGenerator {
	CandidateProfiles profiles=new CandidateProfiles();
	public static final String VALUE_PREFIX_PARAMAETER="value";
	public static final String VALUE_PREFIX_DEFAULT="";
	public String valuePrefix;
	
	public static final String NUMBER_FORMAT_PARAMETER="numberFormat";
	public static final String NUMBER_FORMAT_DEFAULT="%d";
	public String numberFormat;
	
	
	@Override
	public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
		
		super.configure(LongType.INSTANCE, params, serviceRegistry);
		valuePrefix=ConfigurationHelper.getString(VALUE_PREFIX_PARAMAETER, params, VALUE_PREFIX_DEFAULT);
		numberFormat=ConfigurationHelper.getString(NUMBER_FORMAT_PARAMETER, params, NUMBER_FORMAT_DEFAULT);
	}
	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		// TODO Auto-generated method stub
		return valuePrefix+String.format(numberFormat, super.generate(session, object)); 
	}


}