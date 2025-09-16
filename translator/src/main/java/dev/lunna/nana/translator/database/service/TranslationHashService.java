package dev.lunna.nana.translator.database.service;

import dev.lunna.nana.translator.database.HibernateConnection;
import dev.lunna.nana.translator.database.model.TranslationHash;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class TranslationHashService {
    private static final Logger log = LoggerFactory.getLogger(TranslationHashService.class);
    private final HibernateConnection hibernateConnection;

    @Inject
    public TranslationHashService(@NotNull final HibernateConnection hibernateConnection) {
        this.hibernateConnection = hibernateConnection;
    }

    public void save(@NotNull final TranslationHash hash) {
        try (var session = hibernateConnection.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.persist(hash);
            transaction.commit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void batchSave(@NotNull final Iterable<TranslationHash> hashes) {
        try (var session = hibernateConnection.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            for (var hash : hashes) {
                if (getByHash(hash.getHash()) != null) {
                    continue;
                }

                session.persist(hash);

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
    public TranslationHash getById(final int id) {
        try (var session = hibernateConnection.getSessionFactory().openSession()) {
            return session.find(TranslationHash.class, id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    @Nullable
    public TranslationHash getByHash(final int hash) {
        try (var session = hibernateConnection.getSessionFactory().openSession()) {
            var query = session.createQuery("FROM TranslationHash WHERE hash = :hash", TranslationHash.class);
            query.setParameter("hash", hash);
            return query.uniqueResult();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    public boolean insertIfNotExists(@NotNull final TranslationHash hash) {
        if (getByHash(hash.getHash()) != null) {
            return false;
        }

        save(hash);
        return true;
    }
}
