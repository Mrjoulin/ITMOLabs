import unittest

from Lab03 import *


class TestFirstTask(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        with open("smile.txt") as f:
            print(f.read())

    def test_first_one(self):
        text = "Test :<), how it works. :<)\n:<) So that :< end:<)"
        return self.assertEqual(task_one(text), 3)

    def test_first_two(self):
        # Empty test
        text = ""
        return self.assertEqual(task_one(text), 0)

    def test_first_three(self):
        # Smile in start of the text
        text = ":<)\n Very funny text :<)\n:<))"
        return self.assertEqual(task_one(text), 3)

    def test_first_four(self):
        # Part of text
        text = "Since the sequence is strictly increasing (the sign is strictly fulfilled for " \
               "any adjacent members of the sequence:<) so you can perform the operation."

        return self.assertEqual(task_one(text), 0)

    def test_first_five(self):
        # Pass None as param
        text = None
        return self.assertEqual(task_one(text), 0)


class TestSecondTask(unittest.TestCase):
    def test_second_one(self):
        text = "Ходит бродит человечек, он уедит - не вернётся"
        return self.assertEqual(task_two(text), "уедит")

    def test_second_two(self):
        # Word first and last, but last not correct
        text = "Ионный лазер сарботал идеально"
        return self.assertEqual(task_two(text), "Ионный")

    def test_second_three(self):
        # Empty text
        text = ""
        return self.assertEqual(task_two(text), "")

    def test_second_four(self):
        # One incorrect and two correct word one by one
        text = "Юлия выбрала себе интенсивное обучение (в коллежде)"
        return self.assertEqual(task_two(text), "интенсивное\nобучение")

    def test_second_five(self):
        # Check upper case words
        text = "НИУ ИТМО выбрал себе нового председателя студенческого офиса"
        return self.assertEqual(task_two(text), "НИУ")

    def test_second_six(self):
        # Pass None as param
        text = None
        return self.assertEqual(task_two(text), "")


class TestThirdTask(unittest.TestCase):
    def test_third_one(self):
        text = "Хорошо в России жить!"
        return self.assertEqual(task_three(text), "жить\nХорошо")

    def test_third_two(self):
        # Comma after word, words one by one, word in the end
        text = "Каждый охотник желает знать, где сидит фазан"
        return self.assertEqual(task_three(text), "где\nзнать\nсидит\nфазан")

    def test_third_three(self):
        # Empty text
        text = ""
        return self.assertEqual(task_three(text), "")

    def test_third_four(self):
        # All words are correct
        text = "А вот и ты! Как долго я тут жду"
        return self.assertEqual(task_three(text), "А\nи\nя\nты\nвот\nКак\nтут\nжду\nдолго")

    def test_third_five(self):
        # No corrects words
        text = "Выборы очередного кандидата в Студенческий совет затянулись"
        return self.assertEqual(task_three(text), "")

    def test_third_six(self):
        # Pass None as param
        text = None
        return self.assertEqual(task_three(text), "")


if __name__ == '__main__':
    unittest.main()
