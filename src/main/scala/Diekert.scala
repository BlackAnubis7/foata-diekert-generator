
import Dependencies.isD

import scala.annotation.tailrec

object Diekert {
  /**
   * Generuje zredukowany graf Diekerta
   * @param depth głębokość rekurencji, a zarazem indeks wierzchołka grafu
   * @param word słowo, dla którego wyznaczany jest graf
   * @param above dane wygenerowane przez wyższe poziomy rekurencji
   * @param prods lista produkcji
   * @return graf jako tablica krotek:
   *         1. Indeks wierzchołka
   *         1. Indeks klasy
   *         1. Bezpośredni rodzice w drzewie
   *         1. Dalsi przodkowie
   */
  @tailrec
  def graph(depth: Int, word: Array[String], above: Array[(Int, Int, Array[Int], Array[Int])],
            prods: Map[String, String]): Array[(Int, Int, Array[Int], Array[Int])] = {
    if (depth < word.length) {
      val dep = above.filter(t => isD(prods, word(depth), word(t._1)))  // wierzchołki grafu zależne od obecnego
      val ancestors = dep.foldLeft(Array[Int]())((a, t) => a.concat(t._3).concat(t._4)).distinct
      // Jeśli dany wierzczhołek jest przodkiem mojego rodzica, to nie ma sensu żeby był też rodzicem (redukcja)
      val parents = dep.foldLeft(Array[Int]())((a, t) => a.appended(t._1)).distinct.filter(i => !ancestors.contains(i))
      // Indeks klasy jest o 1 większy niż u rodzica o najwyższym indeksie klasy (0 dla wierzchołków bez rodziców)
      val level = above.filter(t => parents.contains(t._1)).foldLeft(0)((x, t) => math.max(x, t._2 + 1))
      graph(depth+1, word, above.appended((depth, level, parents, ancestors)), prods)
    } else above
  }

  /**
   * Inicjalizuje rekurencyjne wykonania funkcji [[graph]]
   * @param word słowo, dla którego wyznaczany jest graf
   * @param prods lista produkcji
   * @return graf jako tablica krotek:
   *         1. Indeks wierzchołka
   *         1. Indeks klasy
   *         1. Bezpośredni rodzice w drzewie
   *         1. Dalsi przodkowie
   */
  def startGraph(word: Array[String], prods: Map[String, String]): Array[(Int, Int, Array[Int], Array[Int])] =
    graph(0, word, Array[(Int, Int, Array[Int], Array[Int])](), prods)

  /**
   * Uzyskuje listę krawędzi skierowanych grafu Diekerta
   * @param graph graf
   * @return tablica krotek (Indeks wierzchołka A, Indeks wierzchołka B)
   */
  def graphEdges(graph: Array[(Int, Int, Array[Int], Array[Int])]): Array[(Int, Int)] =
    graph.foldLeft(Array[(Int, Int)]())((a, t) => a.concat(
      t._3.foldLeft(Array[(Int, Int)]())((a, n) => a.appended((n, t._1)))
    ))
}
