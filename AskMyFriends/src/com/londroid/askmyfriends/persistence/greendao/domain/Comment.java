package com.londroid.askmyfriends.persistence.greendao.domain;

import com.londroid.askmyfriends.persistence.greendao.dao.DaoSession;
import de.greenrobot.dao.DaoException;

import com.londroid.askmyfriends.persistence.greendao.dao.CommentDao;
import com.londroid.askmyfriends.persistence.greendao.dao.JurorDao;
import com.londroid.askmyfriends.persistence.greendao.dao.VoteDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table COMMENT.
 */
public class Comment {

    private Long id;
    private String text;
    private long jurorId;
    private long voteId;
    private long surveyId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient CommentDao myDao;

    private Vote vote;
    private Long vote__resolvedKey;

    private Juror juror;
    private Long juror__resolvedKey;


    public Comment() {
    }

    public Comment(Long id) {
        this.id = id;
    }

    public Comment(Long id, String text, long jurorId, long voteId, long surveyId) {
        this.id = id;
        this.text = text;
        this.jurorId = jurorId;
        this.voteId = voteId;
        this.surveyId = surveyId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCommentDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getJurorId() {
        return jurorId;
    }

    public void setJurorId(long jurorId) {
        this.jurorId = jurorId;
    }

    public long getVoteId() {
        return voteId;
    }

    public void setVoteId(long voteId) {
        this.voteId = voteId;
    }

    public long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(long surveyId) {
        this.surveyId = surveyId;
    }

    /** To-one relationship, resolved on first access. */
    public Vote getVote() {
        long __key = this.voteId;
        if (vote__resolvedKey == null || !vote__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            VoteDao targetDao = daoSession.getVoteDao();
            Vote voteNew = targetDao.load(__key);
            synchronized (this) {
                vote = voteNew;
            	vote__resolvedKey = __key;
            }
        }
        return vote;
    }

    public void setVote(Vote vote) {
        if (vote == null) {
            throw new DaoException("To-one property 'voteId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.vote = vote;
            voteId = vote.getId();
            vote__resolvedKey = voteId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Juror getJuror() {
        long __key = this.jurorId;
        if (juror__resolvedKey == null || !juror__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            JurorDao targetDao = daoSession.getJurorDao();
            Juror jurorNew = targetDao.load(__key);
            synchronized (this) {
                juror = jurorNew;
            	juror__resolvedKey = __key;
            }
        }
        return juror;
    }

    public void setJuror(Juror juror) {
        if (juror == null) {
            throw new DaoException("To-one property 'jurorId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.juror = juror;
            jurorId = juror.getId();
            juror__resolvedKey = jurorId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}