package com.conchordance.music;

import static com.conchordance.music.NoteName.*;
import static org.junit.Assert.assertEquals;

import java.text.MessageFormat;

import org.junit.Test;

import com.conchordance.music.Scale.Mode;


public class ScaleTest {

	@Test 
	public void testGetAMajorScale(){
		String scaleName = "A Major Scale";
		String expected = MessageFormat.format("A0, B0, C{0}1, D1, E1, F{0}1, G{0}1", Note.SHARP);
		String actual = Scale.getMajorScale(new Note(A, 0)).toString();
		assertEquals(scaleName, expected, actual);
	}
	
	@Test 
	public void testGetAMajorScale_Dorian(){
		String scaleName = "A Major Scale - Dorian Mode";
		String expected = MessageFormat.format("B0, C{0}1, D1, E1, F{0}1, G{0}1, A1", Note.SHARP);
		String actual = Scale.getMajorScale(new Note(A, 0),Mode.DORIAN).toString();
		assertEquals(scaleName, expected, actual);
	}
	
	@Test 
	public void testGetAMajorScale_Phrygian(){
		String scaleName = "A Major Scale - Phrygian Mode";
		String expected = MessageFormat.format("C{0}0, D0, E0, F{0}0, G{0}0, A0, B0", Note.SHARP);
		String actual = Scale.getMajorScale(new Note(A, 0),Mode.PHRYGIAN).toString();
		assertEquals(scaleName, expected, actual);
	}
	
	@Test 
	public void testGetAMajorScale_Lydian(){
		String scaleName = "A Major Scale - Lydian Mode";
		String expected = MessageFormat.format("D0, E0, F{0}0, G{0}0, A0, B0, C{0}1", Note.SHARP);
		String actual = Scale.getMajorScale(new Note(A, 0),Mode.LYDIAN).toString();
		assertEquals(scaleName, expected, actual);
	}
	
	@Test 
	public void testGetAMajorScale_Mixolydian(){
		String scaleName = "A Major Scale - Mixolydian Mode";
		String expected = MessageFormat.format("E0, F{0}0, G{0}0, A0, B0, C{0}1, D1", Note.SHARP);
		String actual = Scale.getMajorScale(new Note(A, 0),Mode.MIXOLYDIAN).toString();
		assertEquals(scaleName, expected, actual);
	}
	
	@Test 
	public void testGetAMajorScale_Aeolian(){
		String scaleName = "A Major Scale - Aeolian Mode";
		String expected = MessageFormat.format("F{0}0, G{0}0, A0, B0, C{0}1, D1, E1", Note.SHARP);
		String actual = Scale.getMajorScale(new Note(A, 0),Mode.AEOLIAN).toString();
		assertEquals(scaleName, expected, actual);
	}
	
	@Test 
	public void testGetAMajorScale_Locrian(){
		String scaleName = "A Major Scale - Locrian Mode";
		String expected = MessageFormat.format("G{0}0, A0, B0, C{0}1, D1, E1, F{0}1", Note.SHARP);
		String actual = Scale.getMajorScale(new Note(A, 0),Mode.LOCRIAN).toString();
		assertEquals(scaleName, expected, actual);
	}
	
	@Test 
	public void testGetASharpMajorScale(){
		String scaleName = "A Sharp Major Scale";
		String expected = MessageFormat.format("A{0}0, C1, D1, D{0}1, F1, G1, A1", Note.SHARP);
		String actual = Scale.getMajorScale(new Note(A, 1)).toString();
		assertEquals(scaleName, expected, actual);
	}
	
	@Test 
	public void testGetAFlatMajorScale(){
		String scaleName = "A Flat Major Scale";
		String expected = MessageFormat.format("A{0}0, B{0}0, C1, D{0}1, E{0}1, F1, G1", Note.FLAT);
		String actual = Scale.getMajorScale(new Note(A, -1)).toString();
		assertEquals(scaleName, expected, actual);
	}
	
	@Test
	public void testGetCMajorScale() {
		String scaleName = "C Major Scale";
		String expected = "C0, D0, E0, F0, G0, A0, B0";
		String actual = Scale.getMajorScale(new Note(C, 0)).toString();
		assertEquals(scaleName, expected, actual);
	}
	
	@Test
	public void testGetCMinorScale() {
		String scaleName = "C Minor Scale";
		String expected = MessageFormat.format("C0, D0, D{0}0, F0, G0, G{0}0, A{0}0", Note.SHARP);
		String actual = Scale.getMinorScale(new Note(C,0)).toString();
		assertEquals(scaleName, expected, actual);
	}
	
	@Test
	public void testGetBMajorScale() {
		String scaleName = "B Major Scale";
		String expected = MessageFormat.format("B0, C{0}1, D{0}1, E1, F{0}1, G{0}1, A{0}1", Note.SHARP);
		String actual = Scale.getMajorScale(new Note(B, 0)).toString();
		assertEquals(scaleName, expected, actual);
	}


	@Test
	public void testApplyIntervals() {
		Scale cScale = Scale.getMajorScale(new Note(C, 0));
		Note[] cMajor = cScale.applyIntervals(Interval.UNISON, Interval.MAJOR_THIRD, Interval.PERFECT_FIFTH);
		assertEquals("Major chord intervals applied to C scale", new Note(C, 0), cMajor[0]);
		assertEquals("Major chord intervals applied to C scale", new Note(E, 0), cMajor[1]);
		assertEquals("Major chord intervals applied to C scale", new Note(G, 0), cMajor[2]);
	}

	@Test
	public void testOctaves() {
		Scale aScale = Scale.getMajorScale(new Note(A, 0));
		Note c = aScale.getNote(3);
		assertEquals("Octave rolls over at C", new Note(C, 1, 1), c);
	}

	@Test
	public void testApplyInterval() {
		Scale cScale = Scale.getMajorScale(new Note(C, 0));

		Note cFifth = cScale.applyInterval(Interval.PERFECT_FIFTH);
		assertEquals("Fifth degree of C major scale is G", new Note(G, 0), cFifth);
		
		Note cFlatFlatSeven = cScale.applyInterval(new Interval(6, -2));
		assertEquals("Double-flat-Seventh degree of C major scale is Bbb", new Note(B, -2), cFlatFlatSeven);
	}
}
