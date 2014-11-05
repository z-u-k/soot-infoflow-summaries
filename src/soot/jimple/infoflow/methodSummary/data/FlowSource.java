package soot.jimple.infoflow.methodSummary.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soot.SootField;
import soot.jimple.infoflow.methodSummary.xml.XMLConstants;

/**
 * Representation of a flow source
 * 
 * @author Steven Arzt
 */
public class FlowSource extends AbstractFlowSinkSource {

	public FlowSource(SourceSinkType type, int paramterIdx) {
		super(type, paramterIdx, (SummaryAccessPath) null);
	}
	
	public FlowSource(SourceSinkType type, int paramterIdx,
			SummaryAccessPath accessPath) {
		super(type, paramterIdx, accessPath);
	}
	
	public FlowSource(SourceSinkType type, int parameterIdx,
			List<String> fields) {
		super(type, parameterIdx, fields);
	}
	
	public FlowSource createNewSource(SootField extension){
		SummaryAccessPath ap;
		ap = accessPath.extend(extension);
		return new FlowSource(this.type, this.parameterIdx, ap);
	}
	
	@Override
	public Map<String, String> xmlAttributes() {
		Map<String, String> res = new HashMap<String, String>();
		if (isParameter()) {
			res.put(XMLConstants.ATTRIBUTE_FLOWTYPE, XMLConstants.VALUE_PARAMETER);
			res.put(XMLConstants.ATTRIBUTE_PARAMTER_INDEX, getParameterIndex() + "");
		} else { // isField
			res.put(XMLConstants.ATTRIBUTE_FLOWTYPE, XMLConstants.VALUE_FIELD);
			res.put(XMLConstants.ATTRIBUTE_FIELD, "(this)");
		}
		if(hasAccessPath()){
			res.put(XMLConstants.ATTRIBUTE_ACCESSPATH, getAccessPath().toString());
		}
		return res;
	}

	@Override
	public String toString(){
		if(isParameter())
			return "Parameter " + getParameterIndex() + (accessPath == null ? "" : " " +  accessPath.toString());
		else if(isField())
			return "Field" + (accessPath == null ? "" : " " + accessPath.toString());
		else if(isThis())
			return "THIS";
		else
			return "<unknown>";
	}

	//a this source is a field source with apl = 0
	public boolean isThis()
	{
		return type().equals(SourceSinkType.Field) && !hasAccessPath();
	}
}