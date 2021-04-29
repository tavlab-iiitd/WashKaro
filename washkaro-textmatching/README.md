# A Machine Learning Application for Raising WASH Awareness in the Times of COVID-19 Pandemic

This codebase contains the python scripts for 'A Machine Learning Application for Raising WASH Awareness in the Times of COVID-19 Pandemic'.

## Environment and Installation Steps

```bash
pip install -r requirements.txt
```

## Usage

```bash
from run import *
```


```bash
run_experiment(df,sum_type,distance_type,embedding_type,stoplist_type=True)
```

Here: 
1. df : Dataframe with two columns populated with rows containing pairs of texts that need to be matched.

2. sum_type : Summarization technique used:

    Type1: Summa Summarizer: sum_type='summa'
    
    Type2: Gensim Summarizer: sum_type='gensim'
    
    Type3: PyTextRank Summarizer: sum_type='pytextrank'
    
    Type4: LexRank Summarizer: sum_type='lexrank'
    
    Type5: Reduction Summarizer: sum_type='reduction'
    
    Type3: LSA Summarizer: sum_type='lsa'
    
    Type4: Luhn Summarizer: sum_type='luhn'
    
    Type5: KL Summarizer: summarizer_type='kl'

3. distance_type - distance metric used:
    
    Type1: Cosine: distance_type='avg'
    
    Type2: Word Mover Distance: distance_type='wmd'

5. embedding_type - word embedding type:

    Type1: Word2Vec: embedding_type='w2v'
    
    Type2: Glove: embedding_type='glove'

7. stoplist_type - if stopword removal needed

    Type1: True
    
    Type2: False


## Cite

If our work was helpful in your research, please kindly cite this work:

```
@article{pandey2020machine,
  title={A machine learning application for raising wash awareness in the times of covid-19 pandemic},
  author={Pandey, Rohan and Gautam, Vaibhav and Pal, Ridam and Bandhey, Harsh and Dhingra, Lovedeep Singh and Sharma, Himanshu and Jain, Chirag and Bhagat, Kanav and Patel, Lajjaben and Agarwal, Mudit and others},
  journal={arXiv preprint arXiv:2003.07074},
  year={2020}
}
```

### References

[1] Rehurek, R., & Sojka, P. (2011). Gensim–python framework for vector space modelling. NLP Centre, Faculty of Informatics, Masaryk University, Brno, Czech Republic, 3(2).

[2] Bird, S., Klein, E., & Loper, E. (2009). Natural language processing with Python: analyzing text with the natural language toolkit. " O&#x27;Reilly Media, Inc."

[3] Honnibal, M., & Montani, I. (2017). spaCy 2: Natural language understanding with Bloom embeddings, convolutional neural networks and incremental parsing.

[4] Virtanen, P., Gommers, R., Oliphant, T. E., Haberland, M., Reddy, T., Cournapeau, D., … SciPy 1.0 Contributors. (2020). SciPy 1.0: Fundamental Algorithms for Scientific Computing in Python. Nature Methods, 17, 261–272. https://doi.org/10.1038/s41592-019-0686-2

[5] Pennington J, Socher R, Manning CD. Glove: Global vectors for word representation. InProceedings of the 2014 conference on empirical methods in natural language processing (EMNLP) 2014 Oct (pp. 1532-1543).

[6] Tomas Mikolov, Kai Chen, Greg Corrado, Jeffrey Dean : Efficient Estimation of Word Representations in Vector Space. 
