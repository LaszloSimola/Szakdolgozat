package hu.szakdolgozat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AppState {
    private List<Entity.EntityDTO> entities;
    private List<Attribute.AttributeDTO> attributes;
    private List<Relation.RelationDTO> relations;
    private List<OwnLine.OwnLineDTO> connections;
    private List<SpecializerRelation.SpecializerDTO> specializerRelations;

    @JsonIgnore
    public List<Node> getAllNodes() {
        // Combine all the node lists into one list
        return Arrays.asList(
                getEntityObjects(),
                getAttributeObjects(),
                getRelationObjects(),
                getSpecializerRelationObjects()
        ).stream().flatMap(List::stream).collect(Collectors.toList());
    }
    //specializerRelation
    public List<SpecializerRelation.SpecializerDTO> getSpecializerRelations() {
        return specializerRelations;
    }

    public void setSpecializerRelations(List<SpecializerRelation.SpecializerDTO> specializerRelations) {
        this.specializerRelations = specializerRelations;
    }

    @JsonIgnore
    public void setSpecializerObjects(List<SpecializerRelation> specializerRelations) {
        this.specializerRelations = specializerRelations.stream().map(SpecializerRelation::toDTO).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<SpecializerRelation> getSpecializerRelationObjects() {
        return specializerRelations.stream().map(SpecializerRelation::fromDTO).collect(Collectors.toList());
    }
    // Entities
    public List<Entity.EntityDTO> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity.EntityDTO> entityDTOs) {
        this.entities = entityDTOs;
    }

    @JsonIgnore
    public void setEntityObjects(List<Entity> entities) {
        this.entities = entities.stream().map(Entity::toDTO).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<Entity> getEntityObjects() {
        return entities.stream().map(Entity::fromDTO).collect(Collectors.toList());
    }

    // Attributes
    public List<Attribute.AttributeDTO> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute.AttributeDTO> attributeDTOs) {
        this.attributes = attributeDTOs;
    }

    @JsonIgnore
    public void setAttributeObjects(List<Attribute> attributes) {
        this.attributes = attributes.stream().map(Attribute::toDTO).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<Attribute> getAttributeObjects() {
        return attributes.stream().map(Attribute::fromDTO).collect(Collectors.toList());
    }

    // Relations
    public List<Relation.RelationDTO> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation.RelationDTO> relationDTOs) {
        this.relations = relationDTOs;
    }

    @JsonIgnore
    public void setRelationObjects(List<Relation> relations) {
        this.relations = relations.stream().map(Relation::toDTO).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<Relation> getRelationObjects() {
        return relations.stream().map(Relation::fromDTO).collect(Collectors.toList());
    }

    // OwnLines
    public List<OwnLine.OwnLineDTO> getConnections() {
        return connections;
    }

    public void setConnections(List<OwnLine.OwnLineDTO> connectionDTOs) {
        this.connections = connectionDTOs;
    }

    @JsonIgnore
    public void setConnectionObjects(List<OwnLine> connections) {
        this.connections = connections.stream().map(OwnLine::toDTO).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<OwnLine> getConnectionObjects() {
        return connections.stream().map(OwnLine::fromDTO).collect(Collectors.toList());
    }
}

