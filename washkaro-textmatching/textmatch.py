#All Imports Required
import pandas as pd
from scipy import spatial
from utils import *
from load_embedding import *
    
def matching(sentences1,sentences2,distance_type,embedding_type,stoplist_type):
    sims = []
    if(embedding_type=='w2v'):
        model=load_w2v()
    if(embedding_type=='glove'):
        model=load_glove()
    for (sent1, sent2) in zip(sentences1, sentences2):
        
        tokens1 = sent1.tokens_without_stop if use_stoplist else sent1.tokens
        tokens2 = sent2.tokens_without_stop if use_stoplist else sent2.tokens

        tokens1 = [token for token in tokens1 if token in model]
        tokens2 = [token for token in tokens2 if token in model]

        if len(tokens1) == 0 or len(tokens2) == 0:
            sims.append(0)
            continue
        
        if(distance_type=='avg'):        
            sims.append(1 - spatial.distance.cosine(tokens1, tokens2))
        elif(distance_type=='wmd'):
            sims.append(-model.wmdistance(tokens1, tokens2))
    return sims