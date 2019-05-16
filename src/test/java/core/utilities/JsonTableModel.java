package core.utilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.LinkedHashMap;

public class JsonTableModel{

    private JSONArray columnNames;
    private LinkedHashMap<Integer, JSONArray> rowsData;

    public JsonTableModel(JSONObject json)
    {
        setColumnNames(((JSONArray)json.get("columns")));
        setRowsData(new LinkedHashMap<>());
        JSONArray rows = ((JSONArray) JsonHandler.GetValueJSONObject(json,"rows"));
        for(int i=0; i<rows.size();i++)
        {
            getRowsData().put(i,(JSONArray) rows.get(i));
        }
    }

    public int getRowCount() {
        return getRowsData().size();
    }

    public int getColumnCount() {
        return getColumnNames().size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        JSONArray arrValue = getRowsData().get(rowIndex);
        return arrValue.get(columnIndex);
    }

    public Object getValueAt(int rowIndex, String columnName) {
        int columnIndex = getColumnNames().indexOf(columnName);
        return getValueAt(rowIndex, columnIndex);
    }

    public JSONArray getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(JSONArray columnNames) {
        this.columnNames = columnNames;
    }

    public LinkedHashMap<Integer, JSONArray> getRowsData() {
        return rowsData;
    }

    public void setRowsData(LinkedHashMap<Integer, JSONArray> rowsData) {
        this.rowsData = rowsData;
    }

    public LinkedHashMap<String, Object> getRowData(int index)
    {
        LinkedHashMap<String, Object> row = new LinkedHashMap<>();
        if(rowsData.size() > index) {
            JSONArray arrVal = rowsData.get(index);
            for (int i=0; i< columnNames.size();i++)
            {
                row.put((String)columnNames.get(i),arrVal.get(i));
            }
        }
        return row;
    }
}
