package io.github.programminglife2016.pl1_2016.parser.phylotree;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Serialize the TreeNodeCollection into JSON conforming the API.
 */
public class TreeNodeCollectionSerializer implements JsonSerializer<TreeNodeCollection> {
    @Override
    public JsonElement serialize(TreeNodeCollection treeNodeCollection, Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        JsonArray treeNodes = new JsonArray();
        for (TreeNode treeNode : treeNodeCollection) {
            JsonObject treeNodeObject = new JsonObject();
            treeNodeObject.add("id", new JsonPrimitive(treeNode.getId()));
            treeNodeObject.add("name", new JsonPrimitive(treeNode.getName()));
            JsonArray children = new JsonArray();
            treeNode.getChildren().stream().map(TreeNode::getName).forEach(children::add);
            treeNodeObject.add("children", children);
            if (treeNode.getParent() != null) {
                treeNodeObject.add("parent", new JsonPrimitive(treeNode.getParent().getId()));
            } else {
                treeNodeObject.add("parent", new JsonPrimitive("null"));
            }
            treeNodeObject.add("weight", new JsonPrimitive(treeNode.getWeight()));
            treeNodes.add(treeNodeObject);
        }
        jsonObject.add("treeNodes", treeNodes);
        jsonObject.add("root", new JsonPrimitive(treeNodeCollection.getRoot().getName()));
        return jsonObject;
    }
}
