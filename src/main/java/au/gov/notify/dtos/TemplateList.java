package au.gov.notify.dtos;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TemplateList {
    private List<Template> templates;

    public TemplateList(String content){
        JSONObject data = new JSONObject(content);

        templates =  new ArrayList<>();

        JSONArray templatesData = data.getJSONArray("templates");
        for(int i = 0; i < templatesData.length(); i++){
            JSONObject template = templatesData.getJSONObject(i);
            templates.add(new Template(template));
        }
    }

    public List<Template> getTemplates() {
        return templates;
    }

    @Override
    public String toString() {
        return "TemplateList{" +
                "templates=" + templates +
                '}';
    }
}
