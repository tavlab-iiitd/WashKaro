from textmatch import *

#distance_type='avg'or'wmd'
def run_experiment(df,sum_type,distance_type,embedding_type,stoplist_type=True):
    sentences1 = [Sentence(s,sum_type) for s in df.iloc[:,0]]
    sentences2 = [Sentence(s,sum_type) for s in df.iloc[:,1]]
    sims = matching(sentences1, sentences2, distance_type, embedding_type, stoplist_type)
    df['Similarity']=sims
    return df