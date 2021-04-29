import re
from summa import summarizer
import nltk
from gensim.summarization.summarizer import summarize 
from sumy.parsers.plaintext import PlaintextParser
from sumy.nlp.tokenizers import Tokenizer 
from sumy.summarizers.lex_rank import LexRankSummarizer
from sumy.summarizers.reduction import ReductionSummarizer
from sumy.summarizers.lsa import LsaSummarizer
from sumy.summarizers.kl import KLSummarizer
from sumy.summarizers.luhn import LuhnSummarizer 
import spacy
import pytextrank
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer
from nltk.stem.porter import PorterStemmer

# Type1: Summa Summarizer: summarizer_type='summa'
# Type2: Gensim Summarizer: summarizer_type='gensim'
# Type3: PyTextRank Summarizer: summarizer_type='pytextrank'
# Type4: LexRank Summarizer: summarizer_type='lexrank'
# Type5: Reduction Summarizer: summarizer_type='reduction'
# Type3: LSA Summarizer: summarizer_type='lsa'
# Type4: Luhn Summarizer: summarizer_type='luhn'
# Type5: KL Summarizer: summarizer_type='kl'
def initial_clean(text,sum_type,ratio_val):
    if(sum_type=='summa'):
        if(summarizer.summarize(text,ratio=ratio_val) != ""):
            text = (summarizer.summarize(text,ratio=ratio_val))
    elif(sum_type=='gensim'):
        if(summarize(text,ratio=ratio_val) != ""):
            text = (summarize(text,ratio=ratio_val))
    elif(sum_type=='Pytextrank'):
        if(pytextrank_output(text,ratio_val) != ""):
            text = (pytextrank_output(text,ratio_val))
    else:
        text = sumy_summarizer(text,ratio_val,sum_type)
    text = re.sub("((\S+)?(http(s)?)(\S+))|((\S+)?(www)(\S+))|((\S+)?(\@)(\S+)?)", " ", text)
    text = re.sub("[^a-zA-Z ]", "", text)
    text = text.lower() # lower case the text
    text = nltk.word_tokenize(text)
    return text

def pytextrank_output(text,ratio_val):
    #Insert path to "en_core_web_sm-2.2.5" in the local file
    nlp = spacy.load("insert path here")
    tr = pytextrank.TextRank()
    nlp.add_pipe(tr.PipelineComponent, name="textrank", last=True)
    # add PyTextRank into the spaCy pipeline
    tr = pytextrank.TextRank(logger=None)
    text = text
    bad_chars = [';', ':', '!', "*", '•', '’','\ufeff'] 
    for i in bad_chars : 
        text = text.replace(i, '') 
    doc = nlp(text)
    split=int(len(text.split('.'))*ratio_val)
    list1 = ''
    for sent in doc._.textrank.summary(limit_phrases=split, limit_sentences=split):
        list1=list1+str(sent)
    return list1

#Function to convert list of string into a single string
def convert_to_string(final_sent_list): 
    combined_text = " ".join([sent for sent in final_sent_list])
    return combined_text

#Function to return the sumy based summarization with the specified ration and input text:
def sumy_summarizer(text,ratio,summarizer_type):
    num_sent=int(len(text.split("."))*ratio)
    parser = PlaintextParser.from_string(text,Tokenizer("english"))
    if((summarizer_type=='lexrank') or (summarizer_type=='Lexrank')):
        summarizer_instance = LexRankSummarizer()
    elif((summarizer_type=='reduction') or (summarizer_type=='reduction')):
        summarizer_instance = ReductionSummarizer()
    elif((summarizer_type=='lsa') or (summarizer_type=='LSA')):
        summarizer_instance = LsaSummarizer()
    elif((summarizer_type=='luhn') or (summarizer_type=='Luhn')):
        summarizer_instance = LuhnSummarizer()
    elif((summarizer_type=='KL') or (summarizer_type=='kl')):
        summarizer_instance = KLSummarizer()
    summary_values = summarizer_instance(parser.document,num_sent)
    final_summary = []
    for sent in summary_values:
        final_summary.append(str(sent))
    summary_values = convert_to_string(final_summary)
    return summary_values


#Class sentence is used to initialize a sentence and break it into tokens
class Sentence:
    def __init__(self,sentence,sum_type):
        self.raw = sentence
        normalized_sentence = sentence.replace("'", "'").replace("'", "'")
        self.tokens = apply(normalized_sentence,sum_type)
        self.tokens_without_stop = apply_all(normalized_sentence,sum_type)

def apply(text,sum_type):
    summary = lem_words(stem_words(initial_clean(text,sum_type,0.5)))
    return summary
    
def apply_all(text,sum_type):
    summary = lem_words(stem_words(remove_stop_words(initial_clean(text,sum_type,0.5)))) 
    return summary 

#Lemmatization
def lem_words(text):
    lemmatizer = WordNetLemmatizer()
    try:
        text = [lemmatizer.lemmatize(word) for word in text]
        text = [word for word in text if len(word) > 1]
    except IndexError:
        pass
    return text

#Stemming
def stem_words(text):
    stemmer = PorterStemmer()
    try:
        text = [stemmer.stem(word) for word in text]
        text = [word for word in text if len(word) > 1]  # make sure we have no 1 letter words
    except IndexError:  # the word "oed" broke this, so needed try except
        pass
    return text
    
#Removal of stopwords
def remove_stop_words(text):
    stop_words = stopwords.words('english')
    return [word for word in text if word not in stop_words]
