package core.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.LinkedHashMap;

public class JsonTableModel{

    private ArrayNode columnNames;
    private LinkedHashMap<Integer, ArrayNode> rowsData;

    public JsonTableModel(JsonNode json)
    {
        setColumnNames(((ArrayNode)json.get("columns")));
        setRowsData(new LinkedHashMap<>());
        ArrayNode rows = ((ArrayNode) json.get("rows"));
        for(int i=0; i<rows.size();i++)
        {
            getRowsData().put(i,(ArrayNode) rows.get(i));
        }
    }

    public int getRowCount() {
        return getRowsData().size();
    }

    public int getColumnCount() {
        return getColumnNames().size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        ArrayNode arrValue = getRowsData().get(rowIndex);
        return arrValue.get(columnIndex);
    }

    public Object getValueAt(int rowIndex, String columnName) {
        int i=0;
        for(JsonNode n:getColumnNames()){
            if(n.asText().equals(columnName)){
                break;
            }
            i++;
        }
        return getValueAt(rowIndex, i);
    }

    public ArrayNode getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(ArrayNode columnNames) {
        this.columnNames = columnNames;
    }

    public LinkedHashMap<Integer, ArrayNode> getRowsData() {
        return rowsData;
    }

    public void setRowsData(LinkedHashMap<Integer, ArrayNode> rowsData) {
        this.rowsData = rowsData;
    }

    public LinkedHashMap<String, Object> getRowData(int index)
    {
        LinkedHashMap<String, Object> row = new LinkedHashMap<>();
        if(rowsData.size() > index) {
            ArrayNode arrVal = rowsData.get(index);
            for (int i=0; i< columnNames.size();i++)
            {
                row.put(columnNames.get(i).asText(),arrVal.get(i));
            }
        }
        return row;
    }
}
