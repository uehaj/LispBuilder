class LispListIterator implements Iterator {
  LispList cursor

  LispListIterator(LispList list) {
    cursor = list
  }

  boolean hasNext() {
    cursor != null
  }

  Object next() {
    def result = cursor.car
    cursor = cursor.cdr
    return result
  }

  void remove() {
    throw new UnsupportedOperationException("remove not supported");
  }
}

