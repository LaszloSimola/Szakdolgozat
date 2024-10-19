package hu.szakdolgozat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.Node;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AppState {
    private List<Entity.EntityDTO> entities;
    private List<Attribute.AttributeDTO> attributes;
    private List<Relation.RelationDTO> relations;
    private List<OwnLine.OwnLineDTO> connections;

    public List<Entity.EntityDTO> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities.stream().map(Entity::toDTO).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<Entity> getEntityObjects() {
        return entities.stream().map(Entity::fromDTO).collect(Collectors.toList());
    }

    public List<Attribute.AttributeDTO> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes.stream().map(Attribute::toDTO).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<Attribute> getAttributeObjects() {
        return attributes.stream().map(Attribute::fromDTO).collect(Collectors.toList());
    }

    public List<Relation.RelationDTO> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations.stream().map(Relation::toDTO).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<Relation> getRelationObjects() {
        return relations.stream().map(Relation::fromDTO).collect(Collectors.toList());
    }

    public List<OwnLine.OwnLineDTO> getConnections() {
        return connections;
    }


    public void setConnections(List<OwnLine> connections) {
        this.connections = connections.stream().map(OwnLine::toDTO).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<OwnLine> getConnectionObjects() {
        // Use a lambda expression instead of method reference to pass nodeMap to fromDTO()
        return connections.stream()
                .map(OwnLine::fromDTO) // Pass nodeMap to fromDTO
                .collect(Collectors.toList());
    }
}
