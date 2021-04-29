from gensim.models import KeyedVectors
from gensim.scripts.glove2word2vec import glove2word2vec

def load_w2v():
    # https://drive.google.com/file/d/0B7XkCwpI5KDYNlNUTTlSS21pQmM/edit?usp=sharing (Download link for Word2Vec)
    word2vecpath = os.path.expanduser("insert location of downloaded file here") 
    word2vec = KeyedVectors.load_word2vec_format(word2vecpath, binary=True)
    return word2vec
    
def load_glove():
    #http://nlp.stanford.edu/data/glove.840B.300d.zip (Download link for Glove)
    glovepath = os.path.expanduser("insert location of downloaded file here")
    temp_file = "/tmp/glove.840B.300d.w2v.txt"
    glove2word2vec(glovepath, temp_file)
    glove = gensim.models.KeyedVectors.load_word2vec_format(tmp_file)
    return glove