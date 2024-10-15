package com.api.store.persistence;

import com.api.store.domain.Founder;
import com.api.store.domain.FounderCountByDate;
import com.api.store.domain.repository.FounderRepository;
import com.api.store.persistence.crud.FounderDatabaseRepository;
import com.api.store.persistence.dbo.FounderDBO;
import com.api.store.persistence.mapper.FounderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class FounderDboRepository implements FounderRepository {
    @Autowired
    private FounderDatabaseRepository founderDatabaseRepository;
    @Autowired
    private FounderMapper founderMapper;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Optional<Founder> findByEmail(String email) {
        Optional<FounderDBO> founderDBO = founderDatabaseRepository.findByEmail(email);
        return founderDBO.map(usr -> founderMapper.toFounder(usr));
    }

    @Override
    public Boolean existsByEmail(String email) {
        return founderDatabaseRepository.existsByEmail(email);
    }

    @Override
    public long countByEntryDateBetween(LocalDateTime start, LocalDateTime end) {
        return founderDatabaseRepository.countByEntryDateBetween(start,end);
    }

    @Override
    public Founder save(Founder founder) {
        FounderDBO founderDBO = founderMapper.toFounderDBO(founder);
        return founderMapper.toFounder(founderDatabaseRepository.save(founderDBO));
    }

    @Override
    public long count() {
        return founderDatabaseRepository.count();
    }

    @Override
    public List<FounderCountByDate> countFoundersGroupedByEntryDate(LocalDateTime start, LocalDateTime end) {
        // Define match operation to filter documents by entry date range
        MatchOperation matchOperation = Aggregation.match(Criteria.where("entryDate").gte(start).lt(end));

        // Define a project operation to convert entryDate to a date without time (just year-month-day)
        ProjectionOperation projectToDay = Aggregation.project()
                .andExpression("year(entryDate)").as("year")
                .andExpression("month(entryDate)").as("month")
                .andExpression("dayOfMonth(entryDate)").as("day");

        // Define group operation to group by the transformed entryDate (year-month-day) and count the number of documents
        GroupOperation groupOperation = Aggregation.group("year", "month", "day").count().as("totalFounders");

        // Add a sort operation to order results by year, month, and day
        SortOperation sortOperation = Aggregation.sort(Sort.Direction.ASC, "year", "month", "day");

        // Optionally, add a project operation to format the _id back to a date string or construct a date object
        ProjectionOperation projectBackToDate = Aggregation.project()
                .andExpression("{ $dateFromParts: { 'year': '$_id.year', 'month': '$_id.month', 'day': '$_id.day' } }").as("entryDate")
                .andInclude("totalFounders");

        // Perform the aggregation
        Aggregation aggregation = Aggregation.newAggregation(matchOperation, projectToDay, groupOperation, sortOperation, projectBackToDate);
        AggregationResults<FounderCountByDate> results = mongoTemplate.aggregate(aggregation, FounderDBO.class, FounderCountByDate.class);

        // Return the mapped results
        return results.getMappedResults();
    }
}
