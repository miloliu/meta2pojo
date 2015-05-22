/**
 * 
 */
package schema2pojo;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsonschema2pojo.AbstractAnnotator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JEnumConstant;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;

/**
 * @author Liu Xin (milo.xiaoxin@hotmail.com)
 *
 */
public class DataSchemaAnnotator extends AbstractAnnotator{

	@Override
    public void propertyOrder(JDefinedClass clazz, JsonNode propertiesNode) {
        super.propertyOrder(clazz, propertiesNode);
    }

    @Override
    public void propertyInclusion(JDefinedClass clazz, JsonNode schema) {
        super.propertyInclusion(clazz, schema);
    }

    @Override
    public void propertyField(JFieldVar field, JDefinedClass clazz, String propertyName, JsonNode propertyNode) {
    	super.propertyField(field, clazz, propertyName, propertyNode);
        JsonNode anno = propertyNode.path("annotation");
        if (anno != null) {
        	Iterator<JsonNode> it = anno.elements();
        	String pak = Context.get("config-anntation-package");
        	while (it.hasNext()) {
        		String v = it.next().textValue();
        		if (v != null && !v.isEmpty() && v.contains("List(")) {
        			v = v.substring(v.indexOf("List(".intern()) + "List(".intern().length(), v.length() - 1);
        			String[] as = v.split(",");
        			for (String a : as) {
        				a = a.trim();
        				Pattern p = Pattern.compile("(\\w+)[(](\\S+)[)]");
        				Matcher m = p.matcher(a);
        				m.find();
        				String className = m.group(1);
        				String annoValues = m.group(2);
        				try {
							Class clz = this.getClass().forName(pak + className);
							String[] avs = annoValues.split(",");
							for (String av : avs) {
								if (!av.contains("=")) {
									field.annotate(clz).param("value", av);
								}
								else {
									String[] vv = av.split("=");
									field.annotate(clz).param(vv[0], vv[1]);
								}
							}
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        			}
        			break;
        		}
        	}
        	//field.annotate(clazz)
        }
    }

    @Override
    public void propertyGetter(JMethod getter, String propertyName) {
        super.propertyGetter(getter, propertyName);
    }

    @Override
    public void propertySetter(JMethod setter, String propertyName) {
        super.propertySetter(setter, propertyName);
    }

    @Override
    public void anyGetter(JMethod getter) {
        super.anyGetter(getter);
    }

    @Override
    public void anySetter(JMethod setter) {
        super.anySetter(setter);
    }

    @Override
    public void enumCreatorMethod(JMethod creatorMethod) {
        super.enumCreatorMethod(creatorMethod);
    }

    @Override
    public void enumValueMethod(JMethod valueMethod) {
        super.enumValueMethod(valueMethod);
    }

    @Override
    public void enumConstant(JEnumConstant constant, String value) {
    }

    @Override
    public boolean isAdditionalPropertiesSupported() {
        return true;
    }

    @Override
    public void additionalPropertiesField(JFieldVar field, JDefinedClass clazz, String propertyName) {
        super.additionalPropertiesField(field, clazz, propertyName);
    }

}
