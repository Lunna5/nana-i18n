package dev.lunna.nana.translator.database.service;

import dev.lunna.nana.translator.database.HibernateConnection;
import dev.lunna.nana.translator.database.model.PendingTranslation;
import dev.lunna.nana.translator.job.JobStatus;
import dev.lunna.nana.translator.job.TranslationJobType;
import dev.lunna.nana.translator.tl.Language;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

@Singleton
public class PendingTranslationService {
    private static final Logger log = LoggerFactory.getLogger(PendingTranslationService.class);
    private final HibernateConnection hibernateConnection;

    @Inject
    public PendingTranslationService(@NotNull final HibernateConnection hibernateConnection) {
        this.hibernateConnection = hibernateConnection;
    }

    public void save(@NotNull final PendingTranslation pendingTranslation) {
        try (var session = hibernateConnection.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.persist(pendingTranslation);
            transaction.commit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void batchSave(@NotNull final Iterable<PendingTranslation> pendingTranslations) {
        try (var session = hibernateConnection.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            for (var pendingTranslation : pendingTranslations) {
                session.persist(pendingTranslation);

                if (session.getStatistics().getEntityCount() % 50 == 0) {
                    session.flush();
                    session.clear();
                }
            }
            transaction.commit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Nullable
    public PendingTranslation getById(final Long id) {
        try (var session = hibernateConnection.getSessionFactory().openSession()) {
            return session.find(PendingTranslation.class, id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    public List<PendingTranslation> findByStatusLanguageAndType(
            @NotNull final JobStatus status,
            @NotNull final Language lang,
            @NotNull final TranslationJobType type,
            final int limit) {

        try (var session = hibernateConnection.getSessionFactory().openSession()) {
            var query = session.createQuery(
                    "FROM PendingTranslation " +
                            "WHERE status = :status AND lang = :lang AND type = :type " +
                            "ORDER BY id", PendingTranslation.class);

            query.setParameter("status", status);
            query.setParameter("lang", lang);
            query.setParameter("type", type);
            query.setMaxResults(limit);

            return query.getResultList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return Collections.emptyList();
    }

    public boolean updateStatus(final Long id, @NotNull final JobStatus newStatus) {
        try (var session = hibernateConnection.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();

            var pendingTranslation = session.find(PendingTranslation.class, id);
            if (pendingTranslation == null) {
                transaction.rollback();
                return false;
            }

            pendingTranslation.setStatus(newStatus);
            session.merge(pendingTranslation);
            transaction.commit();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
}